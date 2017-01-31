package org.iatoki.judgels.sealtiel.queue.rabbitmq;

import com.google.inject.AbstractModule;
import org.iatoki.judgels.Config;
import org.iatoki.judgels.play.ApplicationConfig;
import org.iatoki.judgels.sealtiel.queue.QueueService;

public final class RabbitMQModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Config.class).annotatedWith(RabbitMQConfigSource.class).toInstance(ApplicationConfig.getInstance());
        bind(QueueService.class).annotatedWith(RabbitMQService.class).to(RabbitMQ.class);
    }
}
