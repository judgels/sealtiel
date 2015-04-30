package org.iatoki.judgels.sealtiel;

import play.Configuration;

public final class SealtielProperties {
    private static SealtielProperties INSTANCE;

    private final Configuration conf;
    private final String confLocation;

    private String sealtielUsername;
    private String sealtielPassword;

    private String rabbitmqHost;
    private int rabbitmqPort;
    private String rabbitmqUsername;
    private String rabbitmqPassword;
    private String rabbitmqVirtualHost;

    private SealtielProperties(Configuration conf, String confLocation) {
        this.conf = conf;
        this.confLocation = confLocation;
    }

    public static synchronized void buildInstance(Configuration conf, String confLocation) {
        if (INSTANCE != null) {
            throw new UnsupportedOperationException("SealtielProperties instance has already been built");
        }

        INSTANCE = new SealtielProperties(conf, confLocation);
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
        return conf.getString(key);
    }

    private String requireStringValue(String key) {
        String value = getStringValue(key);
        if (value == null) {
            throw new RuntimeException("Missing " + key + " property in " + confLocation);
        }
        return value;
    }

    private Integer getIntegerValue(String key) {
        return conf.getInt(key);
    }

    private int requireIntegerValue(String key) {
        Integer value = getIntegerValue(key);
        if (value == null) {
            throw new RuntimeException("Missing " + key + " property in " + confLocation);
        }
        return value;
    }
}
