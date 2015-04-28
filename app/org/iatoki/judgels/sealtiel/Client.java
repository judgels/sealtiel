package org.iatoki.judgels.sealtiel;

import java.util.List;

public class Client {

    private final long id;

    private final String clientJid;

    private final String secret;

    private final String name;

    private final String adminName;

    private final String adminEmail;

    private final List<String> acquaintances;

    private final long totalDownload;

    private final long lastDownloadTime;

    public Client(long id, String clientJid, String secret, String name, String adminName, String adminEmail, List<String> acquaintances, long totalDownload, long lastDownloadTime) {
        this.id = id;
        this.clientJid = clientJid;
        this.secret = secret;
        this.name = name;
        this.adminName = adminName;
        this.adminEmail = adminEmail;
        this.acquaintances = acquaintances;
        this.totalDownload = totalDownload;
        this.lastDownloadTime = lastDownloadTime;
    }

    public long getId() {
        return id;
    }

    public String getClientJid() {
        return clientJid;
    }

    public String getSecret() {
        return secret;
    }

    public String getName() {
        return name;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public List<String> getAcquaintances() {
        return acquaintances;
    }

    public long getTotalDownload() {
        return totalDownload;
    }

    public long getLastDownloadTime() {
        return lastDownloadTime;
    }
}
