package org.iatoki.judgels.sealtiel.controllers;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.ClientService;
import org.iatoki.judgels.sealtiel.GsonWrapper;
import org.iatoki.judgels.sealtiel.Message;
import org.iatoki.judgels.sealtiel.MessageService;
import org.iatoki.judgels.sealtiel.QueueDeleter;
import org.iatoki.judgels.sealtiel.RabbitmqConnection;
import org.iatoki.judgels.sealtiel.Requeuer;
import org.iatoki.judgels.sealtiel.UnconfirmedMessage;
import org.iatoki.judgels.sealtiel.client.ClientMessage;
import play.Logger;
import play.data.DynamicForm;
import play.db.jpa.Transactional;
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
import java.util.concurrent.TimeUnit;

@Transactional
public class MessageController extends Controller {

    private ScheduledThreadPoolExecutor executorService;
    private Map<Long, ScheduledFuture> requeuers;
    private Map<Long, Long> unconfirmedMessage;
    private MessageService messageService;
    private ClientService clientService;

    public MessageController(MessageService messageService, ClientService clientService, int threadPool) {
        this.messageService = messageService;
        this.clientService = clientService;
        executorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(threadPool);
        requeuers = new HashMap();
        unconfirmedMessage = UnconfirmedMessage.getInstance();
    }

    public Result sendMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");
        System.out.println(clientJid+ " " +clientSecret);
        Client client = clientService.findClientByClientJid(clientJid);
        if (client.getSecret().equals(clientSecret)) {
            Logger.info("================================================");
            Logger.info("SEND MESSAGE");
            Logger.info("================================================");


            try {
                Gson gson = GsonWrapper.getInstance();

                ClientMessage clientMessage = gson.fromJson(params.get("message"), ClientMessage.class);
                clientMessage.setSourceClientJid(client.getClientJid());
                clientMessage.setSourceIPAddress(request().remoteAddress());
                clientMessage.setTimestamp(System.currentTimeMillis() + "");

                Message message = messageService.createMessage(clientMessage.getId(), clientMessage.getSourceClientJid(), clientMessage.getSourceIPAddress(), clientMessage.getTargetClientJid(), clientMessage.getMessageType(), clientMessage.getMessage(), clientMessage.getPriority());
                if (client.getAcquaintances().contains(clientMessage.getTargetClientJid())) {
                    // target verified

                    Channel channel = RabbitmqConnection.getInstance().getChannel();
                    Map<String, Object> args = new HashMap<String, Object>();
                    args.put("x-max-priority", 10);
                    channel.queueDeclare(message.getTargetClientJid(), true, false, false, args);

                    AMQP.BasicProperties props = MessageProperties.PERSISTENT_BASIC.builder().priority(message.getPriority()).build();
                    channel.basicPublish("", message.getTargetClientJid(), props, gson.toJson(message).getBytes());
                    return ok();
                } else if (clientService.findClientByClientJid(clientMessage.getTargetClientJid()) == null) {
                    // either target is for RPC or in other nodes

                    Channel channel = RabbitmqConnection.getInstance().getChannel();
                    Map<String, Object> args = new HashMap<>();
                    args.put("x-max-priority", 10);
                    channel.queueDeclare(message.getTargetClientJid(), true, false, false, args);

                    AMQP.BasicProperties props = MessageProperties.PERSISTENT_BASIC.builder().priority(message.getPriority()).build();
                    channel.basicPublish("", message.getTargetClientJid(), props, gson.toJson(message).getBytes());

                    executorService.schedule(new QueueDeleter(message.getTargetClientJid()), 5, TimeUnit.MINUTES);

                    return ok();
                } else {
                    return badRequest();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return notFound();
            }
        } else {
            return notFound();
        }
    }

    public Result getMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");
        Client client = clientService.findClientByClientJid(clientJid);

        if (client.getSecret().equals(clientSecret)) {
            Logger.info("================================================");
            Logger.info("GET MESSAGE");

            String result = "";

            Channel channel = RabbitmqConnection.getInstance().getChannel();

            try {
                channel.queueDeclare(client.getClientJid(), true, false, false, null);

                GetResponse delivery = channel.basicGet(client.getClientJid(), false);
                if (delivery != null) {
                    result = new String(delivery.getBody());

                    Gson gson = GsonWrapper.getInstance();
                    ClientMessage clientMessage = gson.fromJson(result, ClientMessage.class);

                    unconfirmedMessage.put(clientMessage.getId(), delivery.getEnvelope().getDeliveryTag());
                    requeuers.put(clientMessage.getId(), executorService.schedule(new Requeuer(clientMessage.getId()), 15, TimeUnit.MINUTES));
                } else {
                    Logger.debug("Result gagal");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.info("================================================");

            if (!"".equals(result)) {
                return ok(result);
            } else {
                return notFound();
            }
        } else {
            return notFound();
        }
    }

    public Result confirmMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");
        Client client = clientService.findClientByClientJid(clientJid);

        if (client.getSecret().equals(clientSecret)) {
            Logger.info("================================================");
            Logger.info("CONFIRM MESSAGE "+params.get("messageId"));

            Channel channel = RabbitmqConnection.getInstance().getChannel();

            try {
                long messageId = Long.parseLong(params.get("messageId"));
                channel.basicAck(unconfirmedMessage.get(messageId), false);
                if (requeuers.containsKey(messageId)) {
                    requeuers.get(messageId).cancel(true);
                }
                unconfirmedMessage.put(messageId, null);
                requeuers.put(messageId, null);
                Logger.info("================================================");
                return ok();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.info("================================================");
                return notFound();
            }
        } else {
            return notFound();
        }
    }

    public Result sendRPCMessage() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");
        Client client = clientService.findClientByClientJid(clientJid);

        if (client.getSecret().equals(clientSecret)) {
            Logger.info("================================================");
            Logger.info("SEND RPC MESSAGE");
            Logger.info("================================================");

            String result = "";

            try {
                Gson gson = GsonWrapper.getInstance();

                ClientMessage clientMessage = gson.fromJson(params.get("message"), ClientMessage.class);
                clientMessage.setSourceClientJid(client.getClientJid());
                clientMessage.setSourceIPAddress(request().remoteAddress());
                clientMessage.setTimestamp(new Date().getTime() + "");

                Message message = messageService.createMessage(clientMessage.getId(), clientMessage.getSourceClientJid(), clientMessage.getSourceIPAddress(), clientMessage.getTargetClientJid(), clientMessage.getMessageType(), clientMessage.getMessage(), clientMessage.getPriority());
                if (client.getAcquaintances().contains(clientMessage.getTargetClientJid())) {

                    Channel channel = RabbitmqConnection.getInstance().getChannel();
                    Map<String, Object> args = new HashMap<String, Object>();
                    args.put("x-max-priority", 10);
                    channel.queueDeclare(message.getTargetClientJid(), true, false, false, args);

                    UUID unique_queue = UUID.randomUUID();
                    clientMessage.setSourceClientJid(unique_queue.toString());
                    AMQP.BasicProperties props = MessageProperties.PERSISTENT_BASIC.builder().priority(10).build();

                    channel.basicPublish("", message.getTargetClientJid(), props, gson.toJson(message).getBytes());

                    channel.queueDeclare(unique_queue.toString(), true, false, false, null);
                    GetResponse delivery = channel.basicGet(unique_queue.toString(), true);
                    if (delivery != null) {
                        result = new String(delivery.getBody());

                        channel.queueDelete(unique_queue.toString());
                    } else {
                        Logger.debug("Result gagal");
                    }

                    if (!"".equals(result)) {
                        return ok(result);
                    } else {
                        return notFound();
                    }
                } else {
                    return badRequest();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return notFound();
            }
        } else {
            return notFound();
        }
    }

    public Result extendTimeout() {
        DynamicForm params = DynamicForm.form().bindFromRequest();
        String clientJid = params.get("clientJid");
        String clientSecret = params.get("clientSecret");
        Client client = clientService.findClientByClientJid(clientJid);

        if (client.getSecret().equals(clientSecret)) {
            Logger.info("================================================");
            Logger.info("EXTEND TIMEOUT " + params.get("messageId"));

            Channel channel = RabbitmqConnection.getInstance().getChannel();

            try {
                long messageId = Long.parseLong(params.get("messageId"));
                channel.basicAck(unconfirmedMessage.get(messageId), false);
                if (requeuers.containsKey(messageId)) {
                    requeuers.get(messageId).cancel(true);
                    requeuers.put(messageId, executorService.schedule(new Requeuer(messageId), 15, TimeUnit.MINUTES));
                }
                Logger.info("================================================");
                return ok();
            } catch (IOException e) {
                e.printStackTrace();
                Logger.info("================================================");
                return notFound();
            }
        } else {
            return notFound();
        }
    }
}
