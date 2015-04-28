package org.iatoki.judgels.sealtiel;

import java.io.IOException;

public interface QueueService {

    void createQueue(String queueName) throws IOException;

    void putMessageInQueue(String queueName, int priority, byte[] messages) throws IOException;

    QueueMessage getMessageFromQueue(String queueName) throws IOException;

    void ackMessage(long messageId) throws IOException;

    void requeueMessage(long messageId) throws IOException;

    void deleteQueue(String queueName) throws IOException;
}
