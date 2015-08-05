package org.iatoki.judgels.sealtiel;

import com.typesafe.config.Config;

public final class SealtielProperties {
    private static SealtielProperties INSTANCE;

    private final Config config;

    private String sealtielUsername;
    private String sealtielPassword;

    private String rabbitmqHost;
    private int rabbitmqPort;
    private String rabbitmqUsername;
    private String rabbitmqPassword;
    private String rabbitmqVirtualHost;

    private SealtielProperties(Config config) {
        this.config = config;
    }

    public static synchronized void buildInstance(Config config) {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException("SealtielProperties instance has already been built");
        }

        INSTANCE = new SealtielProperties(config);
        INSTANCE.build();
    }

    public static SealtielProperties getInstance() {
        if (INSTANCE == null) {
            throw new UnsupportedOperationException("SealtielProperties instance has not been built");
        }
        return INSTANCE;
    }

    public String getSealtielUsername() {
        return sealtielUsername;
    }

    public String getSealtielPassword() {
        return sealtielPassword;
    }

    public String getRabbitmqHost() {
        return rabbitmqHost;
    }

    public int getRabbitmqPort() {
        return rabbitmqPort;
    }

    public String getRabbitmqUsername() {
        return rabbitmqUsername;
    }

    public String getRabbitmqPassword() {
        return rabbitmqPassword;
    }

    public String getRabbitmqVirtualHost() {
        return rabbitmqVirtualHost;
    }

    private void build() {
        sealtielUsername = requireStringValue("sealtiel.username");
        sealtielPassword = requireStringValue("sealtiel.password");

        rabbitmqHost = requireStringValue("rabbitmq.host");
        rabbitmqPort = requireIntegerValue("rabbitmq.port");
        rabbitmqUsername = requireStringValue("rabbitmq.username");
        rabbitmqPassword = requireStringValue("rabbitmq.password");
        rabbitmqVirtualHost = requireStringValue("rabbitmq.virtualHost");
    }

    private String getStringValue(String key) {
        if (!config.hasPath(key)) {
            return null;
        }
        return config.getString(key);
    }

    private String requireStringValue(String key) {
        return config.getString(key);
    }

    private Integer getIntegerValue(String key) {
        if (!config.hasPath(key)) {
            return null;
        }
        return config.getInt(key);
    }

    private int requireIntegerValue(String key) {
        return config.getInt(key);
    }
}
