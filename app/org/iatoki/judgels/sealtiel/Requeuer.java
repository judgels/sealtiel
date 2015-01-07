package org.iatoki.judgels.sealtiel;

import com.rabbitmq.client.Channel;

import java.io.IOException;

public class Requeuer implements Runnable {

    private long messageId;

    public Requeuer(long messageId) {
        this.messageId = messageId;
    }

    @Override
    public void run() {
        try {
            Channel channel = RabbitmqConnection.getInstance().getChannel();
            channel.basicNack(UnconfirmedMessage.getInstance().get(messageId), false, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
