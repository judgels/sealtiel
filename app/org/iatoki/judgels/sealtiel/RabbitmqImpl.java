package org.iatoki.judgels.sealtiel;

import com.google.common.collect.Maps;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Map;

public final class RabbitmqImpl implements QueueService {
    private static RabbitmqImpl INSTANCE;

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    private RabbitmqImpl(String host, int port, String username, String password, String virtualHost) {
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
    }

    public synchronized void createConnection() throws IOException {
        if ((connection != null) && (connection.isOpen())) {
            connection.close();
        }
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public boolean isConnected() {
        return ((connection != null) && (channel != null) && (connection.isOpen()) && (channel.isOpen()));
    }

    @Override
    public void createQueue(String queueName) throws IOException {
        if (!isConnected()) {
            createConnection();
        }
        Map<String, Object> args = Maps.newHashMap();
        args.put("x-max-priority", 10);
        channel.queueDeclare(queueName, true, false, false, args);
    }

    @Override
    public void putMessageInQueue(String queueName, int priority, byte[] messages) throws IOException {
        if (!isConnected()) {
            createConnection();
        }
        createQueue(queueName);

        AMQP.BasicProperties props = MessageProperties.PERSISTENT_BASIC.builder().priority(priority).build();
        channel.basicPublish("", queueName, props, messages);
    }

    @Override
    public QueueMessage getMessageFromQueue(String queueName) throws IOException {
        if (!isConnected()) {
            createConnection();
        }
        GetResponse delivery = channel.basicGet(queueName, false);
        if (delivery != null) {
            return new QueueMessage(delivery.getEnvelope().getDeliveryTag(), new String(delivery.getBody()));
        } else {
            return null;
        }
    }

    @Override
    public void ackMessage(long messageId) throws IOException {
        if (!isConnected()) {
            createConnection();
        }
        channel.basicAck(messageId, false);
    }

    @Override
    public void requeueMessage(long messageId) throws IOException {
        if (!isConnected()) {
            createConnection();
        }
        channel.basicNack(messageId, false, true);
    }

    @Override
    public void deleteQueue(String queueName) throws IOException {
        if (!isConnected()) {
            createConnection();
        }
        channel.queueDelete(queueName);
    }

    public synchronized static void buildInstance(String host, int port, String username, String password, String virtualHost) {
        if (INSTANCE == null) {
            INSTANCE = new RabbitmqImpl(host, port, username, password, virtualHost);
        } else {
            throw new IllegalStateException();
        }
    }

    public static RabbitmqImpl getInstance() {
        return INSTANCE;
    }
}
