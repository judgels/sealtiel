package org.iatoki.judgels.sealtiel;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import play.Play;

import java.io.IOException;

public final class RabbitmqConnection {
    private static RabbitmqConnection INSTANCE;

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    private RabbitmqConnection() {
        factory = new ConnectionFactory();
        factory.setHost(SealtielProperties.getInstance().getRabbitmqHost());
        factory.setPort(SealtielProperties.getInstance().getRabbitmqPort());
        factory.setUsername(SealtielProperties.getInstance().getRabbitmqUsername());
        factory.setPassword(SealtielProperties.getInstance().getRabbitmqPassword());
        factory.setVirtualHost(SealtielProperties.getInstance().getRabbitmqVirtualHost());

        boolean check = true;

        while (check) {
            try {
                createConnection();
                check = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createConnection() throws IOException {
        if ((connection != null) && (connection.isOpen())) {
            connection.close();
        }
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public boolean isConnected() {
        return ((connection != null) && (channel != null) && (connection.isOpen()) && (channel.isOpen()));
    }

    public Channel getChannel() {
        return channel;
    }

    public static RabbitmqConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RabbitmqConnection();
        }
        if (!INSTANCE.isConnected()) {
            try {
                INSTANCE.createConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return INSTANCE;
    }
}
