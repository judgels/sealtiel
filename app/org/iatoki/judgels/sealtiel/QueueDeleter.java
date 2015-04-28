package org.iatoki.judgels.sealtiel;

import com.rabbitmq.client.Channel;

import java.io.IOException;

public final class QueueDeleter implements Runnable {

    private final String queueName;

    public QueueDeleter(String queueName) {
        this.queueName = queueName;
    }

    @Override
    public void run() {
        try {
            Channel channel = RabbitmqConnection.getInstance().getChannel();
            channel.queueDelete(queueName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
