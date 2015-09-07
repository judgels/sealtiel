package org.iatoki.judgels.sealtiel.controllers.apis;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.iatoki.judgels.play.IdentityUtils;
import org.iatoki.judgels.play.apis.JudgelsAPIForbiddenException;
import org.iatoki.judgels.play.apis.JudgelsAPIInternalServerErrorException;
import org.iatoki.judgels.play.apis.JudgelsAppClientAPIIdentity;
import org.iatoki.judgels.play.controllers.apis.JudgelsAPIController;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.Message;
import org.iatoki.judgels.sealtiel.QueueMessage;
import org.iatoki.judgels.sealtiel.UnacknowledgedMessages;
import org.iatoki.judgels.sealtiel.config.QueueThreadPool;
import org.iatoki.judgels.sealtiel.config.RabbitMQService;
import org.iatoki.judgels.sealtiel.runnables.Requeuer;
import org.iatoki.judgels.sealtiel.services.ClientService;
import org.iatoki.judgels.sealtiel.services.MessageService;
import org.iatoki.judgels.sealtiel.services.QueueService;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.iatoki.judgels.play.controllers.apis.JudgelsAPIControllerUtils.authenticateAsJudgelsAppClient;
import static org.iatoki.judgels.play.controllers.apis.JudgelsAPIControllerUtils.parseRequestBody;

@Singleton
@Named
@JudgelsAPIController
public final class MessageAPIController extends Controller {

    private final ClientService clientService;
    private final ScheduledThreadPoolExecutor executorService;
    private final MessageService messageService;
    private final QueueService queueService;
    private final Map<Long, ScheduledFuture> requeuers;
    private final Map<Long, Long> unacknowledgedMessages;

    @Inject
    public MessageAPIController(ClientService clientService, MessageService messageService, @RabbitMQService QueueService queueService, @QueueThreadPool int threadPool) {
        this.clientService = clientService;
        this.executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(threadPool);
        this.messageService = messageService;
        this.queueService = queueService;
        this.requeuers = Maps.newHashMap();
        this.unacknowledgedMessages = UnacknowledgedMessages.getInstance();
    }

    @BodyParser.Of(value = BodyParser.Text.class, maxLength = 512 * 1024 * 1024)
    @Transactional
    public Result sendMessage() {
        JudgelsAppClientAPIIdentity identity = authenticateAsJudgelsAppClient(clientService);
        SendMessageRequestBody body = parseRequestBody(SendMessageRequestBody.class);

        Client client = clientService.findClientByJid(identity.getClientJid());
        if (!client.getAcquaintances().contains(body.targetClientJid)) {
            throw new JudgelsAPIForbiddenException("Target client is not an acquaintance of source client");
        }

        Message message = messageService.createMessage(client.getJid(), IdentityUtils.getIpAddress(), body.targetClientJid, body.messageType, body.message, body.priority);
        try {
            queueService.putMessageInQueue(message.getTargetClientJid(), Math.min(Math.max(message.getPriority(), 0), 10), new Gson().toJson(message).getBytes());
        } catch (IOException | TimeoutException e) {
            throw new JudgelsAPIInternalServerErrorException(e);
        }

        return ok();
    }

    @Transactional(readOnly = true)
    public Result fetchMessage() {
        JudgelsAppClientAPIIdentity identity = authenticateAsJudgelsAppClient(clientService);

        Client client = clientService.findClientByJid(identity.getClientJid());

        QueueMessage queueMessage;
        try {
            queueMessage = queueService.getMessageFromQueue(client.getJid());
        } catch (IOException | TimeoutException e) {
            throw new JudgelsAPIInternalServerErrorException(e);
        }

        if (queueMessage == null) {
            return ok();
        }

        Message message = new Gson().fromJson(queueMessage.getContent(), Message.class);
        unacknowledgedMessages.put(message.getId(), queueMessage.getTag());
        requeuers.put(message.getId(), executorService.schedule(new Requeuer(message.getId(), queueService), 15, TimeUnit.MINUTES));

        return ok(queueMessage.getContent());
    }

    @Transactional(readOnly = true)
    public Result acknowledgeMessage(long messageId) {
        authenticateAsJudgelsAppClient(clientService);

        try {
            queueService.ackMessage(unacknowledgedMessages.get(messageId));
        } catch (IOException | TimeoutException e) {
            throw new JudgelsAPIInternalServerErrorException(e);
        }

        if (requeuers.containsKey(messageId)) {
            requeuers.get(messageId).cancel(true);
        }

        unacknowledgedMessages.remove(messageId);
        requeuers.remove(messageId);

        return ok();
    }

    @Transactional(readOnly = true)
    public Result extendTimeout(long messageId) {
        authenticateAsJudgelsAppClient(clientService);

        try {
            queueService.ackMessage(unacknowledgedMessages.get(messageId));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        if (requeuers.containsKey(messageId)) {
            requeuers.get(messageId).cancel(true);
            requeuers.put(messageId, executorService.schedule(new Requeuer(messageId, queueService), 15, TimeUnit.MINUTES));
        }

        return ok();
    }

    static class SendMessageRequestBody {

        public String targetClientJid;
        public String messageType;
        public String message;
        public int priority;
    }
}
