package org.iatoki.judgels.sealtiel.controllers.apis;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.iatoki.judgels.play.JudgelsPlayUtils;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.ClientMessage;
import org.iatoki.judgels.sealtiel.GsonWrapper;
import org.iatoki.judgels.sealtiel.Message;
import org.iatoki.judgels.sealtiel.QueueMessage;
import org.iatoki.judgels.sealtiel.UnconfirmedMessage;
import org.iatoki.judgels.sealtiel.config.QueueThreadPool;
import org.iatoki.judgels.sealtiel.config.RabbitMQService;
import org.iatoki.judgels.sealtiel.runnables.QueueDeleter;
import org.iatoki.judgels.sealtiel.runnables.Requeuer;
import org.iatoki.judgels.sealtiel.services.ClientService;
import org.iatoki.judgels.sealtiel.services.MessageService;
import org.iatoki.judgels.sealtiel.services.QueueService;
import play.data.DynamicForm;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Named
public final class MessageAPIController extends Controller {

    private final ClientService clientService;
    private final ScheduledThreadPoolExecutor executorService;
    private final MessageService messageService;
    private final QueueService queueService;
    private final Map<Long, ScheduledFuture> requeuers;
    private final Map<Long, Long> unconfirmedMessage;

    @Inject
    public MessageAPIController(ClientService clientService, MessageService messageService, @RabbitMQService QueueService queueService, @QueueThreadPool int threadPool) {
        this.clientService = clientService;
        this.messageService = messageService;
        this.queueService = queueService;
        executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(threadPool);
        requeuers = Maps.newHashMap();
        unconfirmedMessage = UnconfirmedMessage.getInstance();
    }

    @BodyParser.Of(value = BodyParser.FormUrlEncoded.class, maxLength = 512 * 1024 * 1024)
    @Transactional
    public Result sendMessage() {
        UsernamePasswordCredentials credentials = JudgelsPlayUtils.parseBasicAuthFromRequest(request());

        if (credentials == null) {
            response().setHeader("WWW-Authenticate", "Basic realm=\"" + request().host() + "\"");
            return unauthorized();
        }

        String clientJid = credentials.getUserName();
        String clientSecret = credentials.getPassword();

        DynamicForm dForm = DynamicForm.form().bindFromRequest();
        if (!clientService.existsByClientJid(clientJid)) {
            return notFound();
        }

        Client client = clientService.findClientByJid(clientJid);
        if (!client.getSecret().equals(clientSecret)) {
            return forbidden();
        }

        Gson gson = GsonWrapper.getInstance();

        ClientMessage clientMessage = gson.fromJson(dForm.get("message"), ClientMessage.class);
        clientMessage.setSourceClientJid(client.getJid());
        clientMessage.setSourceIPAddress(request().remoteAddress());
        clientMessage.setTimestamp(System.currentTimeMillis() + "");

        Message message = messageService.createMessage(clientMessage.getId(), clientMessage.getSourceClientJid(), clientMessage.getSourceIPAddress(), clientMessage.getTargetClientJid(), clientMessage.getMessageType(), clientMessage.getMessage(), clientMessage.getPriority());
        if (client.getAcquaintances().contains(clientMessage.getTargetClientJid())) {
            // target verified

            try {
                queueService.putMessageInQueue(message.getTargetClientJid(), Math.min(Math.max(message.getPriority(), 0), 10), gson.toJson(message).getBytes());
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
            return ok();
        } else if (clientService.findClientByJid(clientMessage.getTargetClientJid()) == null) {
            // either target is for RPC or in other nodes

            try {
                queueService.putMessageInQueue(message.getTargetClientJid(), Math.min(Math.max(message.getPriority(), 0), 10), gson.toJson(message).getBytes());
            } catch (IOException | TimeoutException e) {
                throw new RuntimeException(e);
            }
            executorService.schedule(new QueueDeleter(message.getTargetClientJid(), queueService), 5, TimeUnit.MINUTES);

            return ok();
        } else {
            return badRequest();
        }
    }

    @Transactional(readOnly = true)
    public Result getMessage() {
        UsernamePasswordCredentials credentials = JudgelsPlayUtils.parseBasicAuthFromRequest(request());

        if (credentials == null) {
            response().setHeader("WWW-Authenticate", "Basic realm=\"" + request().host() + "\"");
            return unauthorized();
        }

        String clientJid = credentials.getUserName();
        String clientSecret = credentials.getPassword();

        if (!clientService.existsByClientJid(clientJid)) {
            return notFound();
        }

        Client client = clientService.findClientByJid(clientJid);

        if (!client.getSecret().equals(clientSecret)) {
            return forbidden();
        }

        QueueMessage queueMessage;
        try {
            queueMessage = queueService.getMessageFromQueue(client.getJid());
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        String result = "";
        if (queueMessage != null) {
            Gson gson = GsonWrapper.getInstance();
            ClientMessage clientMessage = gson.fromJson(queueMessage.getContent(), ClientMessage.class);
            unconfirmedMessage.put(clientMessage.getId(), queueMessage.getTag());
            requeuers.put(clientMessage.getId(), executorService.schedule(new Requeuer(clientMessage.getId(), queueService), 15, TimeUnit.MINUTES));

            result = queueMessage.getContent();
        }

        if (!"".equals(result)) {
            return ok(result);
        } else {
            return notFound();
        }
    }

    @Transactional(readOnly = true)
    public Result confirmMessage() {
        UsernamePasswordCredentials credentials = JudgelsPlayUtils.parseBasicAuthFromRequest(request());

        if (credentials == null) {
            response().setHeader("WWW-Authenticate", "Basic realm=\"" + request().host() + "\"");
            return unauthorized();
        }

        String clientJid = credentials.getUserName();
        String clientSecret = credentials.getPassword();

        DynamicForm dForm = DynamicForm.form().bindFromRequest();
        if (!clientService.existsByClientJid(clientJid)) {
            return notFound();
        }

        Client client = clientService.findClientByJid(clientJid);

        if (!client.getSecret().equals(clientSecret)) {
            return forbidden();
        }

        long messageId = Long.parseLong(dForm.get("messageId"));
        try {
            queueService.ackMessage(unconfirmedMessage.get(messageId));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        if (requeuers.containsKey(messageId)) {
            requeuers.get(messageId).cancel(true);
        }
        unconfirmedMessage.put(messageId, null);
        requeuers.put(messageId, null);
        return ok();
    }

    @Transactional
    public Result sendRPCMessage() {
        UsernamePasswordCredentials credentials = JudgelsPlayUtils.parseBasicAuthFromRequest(request());

        if (credentials != null) {
            response().setHeader("WWW-Authenticate", "Basic realm=\"" + request().host() + "\"");
            return unauthorized();
        }

        String clientJid = credentials.getUserName();
        String clientSecret = credentials.getPassword();

        DynamicForm dForm = DynamicForm.form().bindFromRequest();
        if (!clientService.existsByClientJid(clientJid)) {
            return notFound();
        }

        Client client = clientService.findClientByJid(clientJid);

        if (!client.getSecret().equals(clientSecret)) {
            return forbidden();
        }

        String result = "";

        Gson gson = GsonWrapper.getInstance();

        ClientMessage clientMessage = gson.fromJson(dForm.get("message"), ClientMessage.class);
        clientMessage.setSourceClientJid(client.getJid());
        clientMessage.setSourceIPAddress(request().remoteAddress());
        clientMessage.setTimestamp(new Date().getTime() + "");

        Message message = messageService.createMessage(clientMessage.getId(), clientMessage.getSourceClientJid(), clientMessage.getSourceIPAddress(), clientMessage.getTargetClientJid(), clientMessage.getMessageType(), clientMessage.getMessage(), clientMessage.getPriority());
        if (!client.getAcquaintances().contains(clientMessage.getTargetClientJid())) {
            return badRequest();
        }

        UUID uniqueQueue = UUID.randomUUID();
        try {
            queueService.createQueue(uniqueQueue.toString());
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return notFound();
        }

        clientMessage.setSourceClientJid(uniqueQueue.toString());
        try {
            queueService.putMessageInQueue(message.getTargetClientJid(), Math.min(Math.max(message.getPriority(), 0), 10), gson.toJson(message).getBytes());
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return notFound();
        }

        QueueMessage queueMessage;
        try {
            queueMessage = queueService.getMessageFromQueue(uniqueQueue.toString());
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return notFound();
        }

        if (queueMessage != null) {
            result = new String(queueMessage.getContent());
            try {
                queueService.deleteQueue(uniqueQueue.toString());
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
                return notFound();
            }
        }

        if (!"".equals(result)) {
            return ok(result);
        } else {
            return notFound();
        }
    }

    @Transactional(readOnly = true)
    public Result extendTimeout() {
        UsernamePasswordCredentials credentials = JudgelsPlayUtils.parseBasicAuthFromRequest(request());

        if (credentials == null) {
            response().setHeader("WWW-Authenticate", "Basic realm=\"" + request().host() + "\"");
            return unauthorized();
        }
        String clientJid = credentials.getUserName();
        String clientSecret = credentials.getPassword();

        if (!clientService.existsByClientJid(clientJid)) {
            return notFound();
        }
        Client client = clientService.findClientByJid(clientJid);

        if (!client.getSecret().equals(clientSecret)) {
            return forbidden();
        }

        DynamicForm dForm = DynamicForm.form().bindFromRequest();
        long messageId = Long.parseLong(dForm.get("messageId"));
        try {
            queueService.ackMessage(unconfirmedMessage.get(messageId));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }

        if (requeuers.containsKey(messageId)) {
            requeuers.get(messageId).cancel(true);
            requeuers.put(messageId, executorService.schedule(new Requeuer(messageId, queueService), 15, TimeUnit.MINUTES));
        }
        return ok();
    }
}
