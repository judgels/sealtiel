package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.sealtiel.models.domains.ClientModel;

public class Client {

    private long id;

    private String clientId;

    private String secret;

    private String channel;

    private String name;

    private String adminName;

    private String adminEmail;

    private String acquaintances;

    private long totalDownload;

    private long lastDownloadTime;

    public Client(ClientModel clientModel) {
        this.id = clientModel.id;
        this.clientId = clientModel.jid;
        this.secret = clientModel.secret;
        this.channel = clientModel.channel;
        this.name = clientModel.name;
        this.adminName = clientModel.adminName;
        this.adminEmail = clientModel.adminEmail;
        this.acquaintances = clientModel.acquaintances;
        this.totalDownload = clientModel.totalDownload;
        this.lastDownloadTime = clientModel.lastDownloadTime;
    }

    public long getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecret() {
        return secret;
    }

    public String getChannel() {
        return channel;
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

    public String getAcquaintances() {
        return acquaintances;
    }

    public long getTotalDownload() {
        return totalDownload;
    }

    public long getLastDownloadTime() {
        return lastDownloadTime;
    }
}
