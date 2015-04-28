package org.iatoki.judgels.sealtiel;

import java.io.IOException;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
