package org.iatoki.judgels.sealtiel;

import java.io.IOException;

public final class Requeuer implements Runnable {

    private final QueueService queueService;
    private final long messageId;

    public Requeuer(QueueService queueService, long messageId) {
        this.queueService = queueService;
        this.messageId = messageId;
    }

    @Override
    public void run() {
        try {
            queueService.requeueMessage(UnconfirmedMessage.getInstance().get(messageId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
