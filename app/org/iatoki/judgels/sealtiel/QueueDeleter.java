package org.iatoki.judgels.sealtiel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public final class QueueDeleter implements Runnable {

    private final QueueService queueService;
    private final String queueName;

    public QueueDeleter(QueueService queueService, String queueName) {
        this.queueService = queueService;
        this.queueName = queueName;
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
