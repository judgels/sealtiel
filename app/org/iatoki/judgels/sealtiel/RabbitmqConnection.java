package org.iatoki.judgels.sealtiel;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import play.Play;

import java.io.IOException;

public class RabbitmqConnection {
    private static RabbitmqConnection INSTANCE;

    private ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    private RabbitmqConnection() {
        factory = new ConnectionFactory();
        factory.setHost(Play.application().configuration().getString("rabbitmq.host"));
        factory.setPort(Play.application().configuration().getInt("rabbitmq.port"));
        factory.setUsername(Play.application().configuration().getString("rabbitmq.username"));
        factory.setPassword(Play.application().configuration().getString("rabbitmq.password"));
        factory.setVirtualHost(Play.application().configuration().getString("rabbitmq.virtualHost"));

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
        if ((connection!=null) && (connection.isOpen())) {
            connection.close();
        }
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public boolean isConnected() {
        return ((connection!=null) && (connection.isOpen()) && (channel.isOpen()));
    }

    public Channel getChannel() {
        return channel;
    }

    public static RabbitmqConnection getInstance() {
        if (INSTANCE==null) {
            INSTANCE = new RabbitmqConnection();
        }
        if (!INSTANCE.isConnected()) {
            boolean check = true;

            while (check) {
                try {
                    INSTANCE.createConnection();
                    check = false;
                } catch (IOException e) {

                }
            }
        }
        return INSTANCE;
    }
}
