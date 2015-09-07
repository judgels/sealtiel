package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.play.JudgelsAppClient;

import java.util.List;

public final class Client implements JudgelsAppClient {

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

    @Override
    public String getJid() {
        return jid;
    }

    @Override
    public String getSecret() {
        return secret;
    }

    @Override
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
