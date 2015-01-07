package org.iatoki.judgels.sealtiel;

import com.rabbitmq.client.QueueingConsumer;

public class AckContainer {
    public QueueingConsumer consumer;
    public long deliveryTag;

    public AckContainer(QueueingConsumer consumer, long deliveryTag) {
        this.consumer = consumer;
        this.deliveryTag = deliveryTag;
    }
}