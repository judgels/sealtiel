package org.iatoki.judgels.sealtiel.runnables;

import org.iatoki.judgels.sealtiel.services.QueueService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class QueueDeleter implements Runnable {

    private final String queueName;
    private final QueueService queueService;

    public QueueDeleter(String queueName, QueueService queueService) {
        this.queueName = queueName;
        this.queueService = queueService;
    }

    @Override
    public void run() {
        try {
            queueService.deleteQueue(queueName);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
