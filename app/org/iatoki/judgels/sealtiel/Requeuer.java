package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.sealtiel.services.QueueService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
