package org.iatoki.judgels.sealtiel.runnables;

import org.iatoki.judgels.sealtiel.UnacknowledgedMessages;
import org.iatoki.judgels.sealtiel.services.QueueService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class Requeuer implements Runnable {

    private final long messageId;
    private final QueueService queueService;

    public Requeuer(long messageId, QueueService queueService) {
        this.messageId = messageId;
        this.queueService = queueService;
    }

    @Override
    public void run() {
        try {
            queueService.requeueMessage(UnacknowledgedMessages.getInstance().get(messageId));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
