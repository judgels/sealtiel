package org.iatoki.judgels.sealtiel.queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface QueueService {

    void createQueue(String queueName) throws IOException, TimeoutException;

    void putMessageInQueue(String queueName, int priority, byte[] messages) throws IOException, TimeoutException;

    QueueMessage getMessageFromQueue(String queueName) throws IOException, TimeoutException;

    void ackMessage(long messageId) throws IOException, TimeoutException;

    void requeueMessage(long messageId) throws IOException, TimeoutException;

    void deleteQueue(String queueName) throws IOException, TimeoutException;
}
