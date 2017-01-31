package org.iatoki.judgels.sealtiel.queue.rabbitmq;

import org.iatoki.judgels.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class RabbitMQConfig {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String virtualHost;

    @Inject
    public RabbitMQConfig(@RabbitMQConfigSource Config config) {
        this.host = config.requireString("rabbitmq.host");
        this.port = config.requireInt("rabbitmq.port");
        this.username = config.requireString("rabbitmq.username");
        this.password = config.requireString("rabbitmq.password");
        this.virtualHost = config.requireString("rabbitmq.virtualHost");
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }
}
