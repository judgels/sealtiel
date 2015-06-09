package org.iatoki.judgels.sealtiel.controllers.apis;

import com.google.gson.Gson;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.ClientService;
import org.iatoki.judgels.sealtiel.GsonWrapper;
import org.iatoki.judgels.sealtiel.Message;
import org.iatoki.judgels.sealtiel.MessageService;
import org.iatoki.judgels.sealtiel.QueueDeleter;
import org.iatoki.judgels.sealtiel.QueueMessage;
import org.iatoki.judgels.sealtiel.QueueService;
import org.iatoki.judgels.sealtiel.Requeuer;
import org.iatoki.judgels.sealtiel.UnconfirmedMessage;
import org.iatoki.judgels.sealtiel.client.ClientMessage;
import play.data.DynamicForm;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

@Transactional
public final class MessageAPIController extends Controller {

    private final ScheduledThreadPoolExecutor executorService;
    private final Map<Long, ScheduledFuture> requeuers;
    private final Map<Long, Long> unconfirmedMessage;
    private final MessageService messageService;
    private final ClientService clientService;
    private final QueueService queueService;

    public MessageAPIController(MessageService messageService, ClientService clientService, QueueService queueService, int threadPool) {
        this.messageService = messageService;
        this.clientService = clientService;
        this.queueService = queueService;
        executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(threadPool);
        requeuers = new HashMap();
        unconfirmedMessage = UnconfirmedMessage.getInstance();
    }

    @BodyParser.Of(value = BodyParser.FormUrlEncoded.class, maxLength = 512 * 1024 * 1024)
    public Result sendMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");
        if (clientService.existByClientJid(clientJid)) {
            Client client = clientService.findClientByClientJid(clientJid);
            if (client.getSecret().equals(clientSecret)) {
                try {
                    Gson gson = GsonWrapper.getInstance();

                    ClientMessage clientMessage = gson.fromJson(params.get("message"), ClientMessage.class);
                    clientMessage.setSourceClientJid(client.getJid());
                    clientMessage.setSourceIPAddress(request().remoteAddress());
                    clientMessage.setTimestamp(System.currentTimeMillis() + "");

                    Message message = messageService.createMessage(clientMessage.getId(), clientMessage.getSourceClientJid(), clientMessage.getSourceIPAddress(), clientMessage.getTargetClientJid(), clientMessage.getMessageType(), clientMessage.getMessage(), clientMessage.getPriority());
                    if (client.getAcquaintances().contains(clientMessage.getTargetClientJid())) {
                        // target verified

                        queueService.putMessageInQueue(message.getTargetClientJid(), Math.min(Math.max(message.getPriority(), 0), 10), gson.toJson(message).getBytes());
                        return ok();
                    } else if (clientService.findClientByClientJid(clientMessage.getTargetClientJid()) == null) {
                        // either target is for RPC or in other nodes

                        queueService.putMessageInQueue(message.getTargetClientJid(), Math.min(Math.max(message.getPriority(), 0), 10), gson.toJson(message).getBytes());
                        executorService.schedule(new QueueDeleter(queueService, message.getTargetClientJid()), 5, TimeUnit.MINUTES);

                        return ok();
                    } else {
                        return badRequest();
                    }
                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return forbidden();
            }
        } else {
            return notFound();
        }
    }

    public Result getMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");

        if (clientService.existByClientJid(clientJid)) {
            Client client = clientService.findClientByClientJid(clientJid);

            if (client.getSecret().equals(clientSecret)) {
                String result = "";

                try {
                    QueueMessage queueMessage = queueService.getMessageFromQueue(client.getJid());

                    if (queueMessage != null) {
                        Gson gson = GsonWrapper.getInstance();
                        ClientMessage clientMessage = gson.fromJson(queueMessage.getContent(), ClientMessage.class);
                        unconfirmedMessage.put(clientMessage.getId(), queueMessage.getTag());
                        requeuers.put(clientMessage.getId(), executorService.schedule(new Requeuer(queueService, clientMessage.getId()), 15, TimeUnit.MINUTES));

                        result = queueMessage.getContent();
                    }
                } catch (IOException  | TimeoutException e) {
                    throw new RuntimeException(e);
                }

                if (!"".equals(result)) {
                    return ok(result);
                } else {
                    return notFound();
                }
            } else {
                return forbidden();
            }
        } else {
            return notFound();
        }
    }

    public Result confirmMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");

        if (clientService.existByClientJid(clientJid)) {
            Client client = clientService.findClientByClientJid(clientJid);

            if (client.getSecret().equals(clientSecret)) {
                try {
                    long messageId = Long.parseLong(params.get("messageId"));
                    queueService.ackMessage(unconfirmedMessage.get(messageId));
                    if (requeuers.containsKey(messageId)) {
                        requeuers.get(messageId).cancel(true);
                    }
                    unconfirmedMessage.put(messageId, null);
                    requeuers.put(messageId, null);
                    return ok();
                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return forbidden();
            }
        } else {
            return notFound();
        }
    }

    public Result sendRPCMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");

        if (clientService.existByClientJid(clientJid)) {
            Client client = clientService.findClientByClientJid(clientJid);

            if (client.getSecret().equals(clientSecret)) {
                String result = "";

                try {
                    Gson gson = GsonWrapper.getInstance();

                    ClientMessage clientMessage = gson.fromJson(params.get("message"), ClientMessage.class);
                    clientMessage.setSourceClientJid(client.getJid());
                    clientMessage.setSourceIPAddress(request().remoteAddress());
                    clientMessage.setTimestamp(new Date().getTime() + "");

                    Message message = messageService.createMessage(clientMessage.getId(), clientMessage.getSourceClientJid(), clientMessage.getSourceIPAddress(), clientMessage.getTargetClientJid(), clientMessage.getMessageType(), clientMessage.getMessage(), clientMessage.getPriority());
                    if (client.getAcquaintances().contains(clientMessage.getTargetClientJid())) {

                        UUID uniqueQueue = UUID.randomUUID();
                        queueService.createQueue(uniqueQueue.toString());

                        clientMessage.setSourceClientJid(uniqueQueue.toString());
                        queueService.putMessageInQueue(message.getTargetClientJid(), Math.min(Math.max(message.getPriority(), 0), 10), gson.toJson(message).getBytes());

                        QueueMessage queueMessage = queueService.getMessageFromQueue(uniqueQueue.toString());
                        if (queueMessage != null) {
                            result = new String(queueMessage.getContent());
                            queueService.deleteQueue(uniqueQueue.toString());
                        }

                        if (!"".equals(result)) {
                            return ok(result);
                        } else {
                            return notFound();
                        }
                    } else {
                        return badRequest();
                    }
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                    return notFound();
                }
            } else {
                return forbidden();
            }
        } else {
            return notFound();
        }
    }

    public Result extendTimeout() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");

        if (clientService.existByClientJid(clientJid)) {
            Client client = clientService.findClientByClientJid(clientJid);

            if (client.getSecret().equals(clientSecret)) {
                try {
                    long messageId = Long.parseLong(params.get("messageId"));
                    queueService.ackMessage(unconfirmedMessage.get(messageId));

                    if (requeuers.containsKey(messageId)) {
                        requeuers.get(messageId).cancel(true);
                        requeuers.put(messageId, executorService.schedule(new Requeuer(queueService, messageId), 15, TimeUnit.MINUTES));
                    }
                    return ok();
                } catch (IOException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return forbidden();
            }
        } else {
            return notFound();
        }
    }
}
