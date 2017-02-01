package org.iatoki.judgels.sealtiel.api.client.v1;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.iatoki.judgels.play.IdentityUtils;
import org.iatoki.judgels.play.api.JudgelsAPIForbiddenException;
import org.iatoki.judgels.play.api.JudgelsAPIInternalServerErrorException;
import org.iatoki.judgels.play.api.JudgelsAppClientAPIIdentity;
import org.iatoki.judgels.play.controllers.apis.AbstractJudgelsAPIController;
import org.iatoki.judgels.sealtiel.api.object.v1.ClientMessageV1;
import org.iatoki.judgels.sealtiel.api.object.v1.ServerMessageV1;
import org.iatoki.judgels.sealtiel.client.Client;
import org.iatoki.judgels.sealtiel.client.ClientService;
import org.iatoki.judgels.sealtiel.message.Message;
import org.iatoki.judgels.sealtiel.message.MessageService;
import org.iatoki.judgels.sealtiel.queue.QueueMessage;
import org.iatoki.judgels.sealtiel.queue.QueueService;
import org.iatoki.judgels.sealtiel.queue.QueueThreadPool;
import org.iatoki.judgels.sealtiel.queue.Requeuer;
import org.iatoki.judgels.sealtiel.queue.UnacknowledgedMessages;
import org.iatoki.judgels.sealtiel.queue.rabbitmq.RabbitMQService;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Singleton
public final class ClientMessageAPIControllerV1 extends AbstractJudgelsAPIController {

    private final ClientService clientService;
    private final ScheduledThreadPoolExecutor executorService;
    private final MessageService messageService;
    private final QueueService queueService;
    private final Map<Long, ScheduledFuture> requeuers;
    private final Map<Long, Long> unacknowledgedMessages;

    @Inject
    public ClientMessageAPIControllerV1(ClientService clientService, MessageService messageService, @RabbitMQService QueueService queueService, @QueueThreadPool int queueThreadPool) {
        this.clientService = clientService;
        this.executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(queueThreadPool);
        this.messageService = messageService;
        this.queueService = queueService;
        this.requeuers = Maps.newHashMap();
        this.unacknowledgedMessages = UnacknowledgedMessages.getInstance();
    }

    @BodyParser.Of(value = BodyParser.Text.class, maxLength = 512 * 1024 * 1024)
    @Transactional
    public Result sendMessage() {
        JudgelsAppClientAPIIdentity identity = authenticateAsJudgelsAppClient(clientService);
        ClientMessageV1 requestBody = parseRequestBody(ClientMessageV1.class);

        Client client = clientService.findClientByJid(identity.getClientJid());
        if (!client.getAcquaintances().contains(requestBody.targetClientJid)) {
            throw new JudgelsAPIForbiddenException("Target client is not an acquaintance of source client");
        }

        Message message = messageService.createMessage(client.getJid(), IdentityUtils.getIpAddress(), requestBody.targetClientJid, requestBody.messageType, requestBody.message, requestBody.priority);
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

        ServerMessageV1 responseBody = new ServerMessageV1();
        responseBody.id = message.getId();
        responseBody.sourceClientJid = message.getSourceClientJid();
        responseBody.sourceIPAddress = message.getSourceIPAddress();
        responseBody.messageType = message.getMessageType();
        responseBody.message = message.getMessage();
        responseBody.timestamp = message.getTimestamp();

        return ok(new Gson().toJson(responseBody));
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
            throw new JudgelsAPIInternalServerErrorException(e);
        }

        if (requeuers.containsKey(messageId)) {
            requeuers.get(messageId).cancel(true);
            requeuers.put(messageId, executorService.schedule(new Requeuer(messageId, queueService), 15, TimeUnit.MINUTES));
        }

        return ok();
    }
}
