package org.iatoki.judgels.sealtiel;

import java.util.List;

public class Client {

    private final long id;

    private final String jid;

    private final String secret;

    private final String name;

    private final List<String> acquaintances;

    private final long totalDownload;

    private final long lastDownloadTime;

    public Client(long id, String jid, String secret, String name, List<String> acquaintances, long totalDownload, long lastDownloadTime) {
        this.id = id;
        this.jid = jid;
        this.secret = secret;
        this.name = name;
        this.acquaintances = acquaintances;
        this.totalDownload = totalDownload;
        this.lastDownloadTime = lastDownloadTime;
    }

    public long getId() {
        return id;
    }

    public String getJid() {
        return jid;
    }

    public String getSecret() {
        return secret;
    }

    public String getName() {
        return name;
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
