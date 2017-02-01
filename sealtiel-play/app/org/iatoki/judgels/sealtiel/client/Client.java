package org.iatoki.judgels.sealtiel.client;

import org.iatoki.judgels.play.api.JudgelsAppClient;

import java.util.List;

public final class Client implements JudgelsAppClient {

    private final long id;
    private final String jid;
    private final String secret;
    private final String name;
    private final List<String> acquaintances;

    public Client(long id, String jid, String secret, String name, List<String> acquaintances) {
        this.id = id;
        this.jid = jid;
        this.secret = secret;
        this.name = name;
        this.acquaintances = acquaintances;
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
}
