package org.iatoki.judgels.sealtiel.account;

import org.iatoki.judgels.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class AccountConfig {

    private final String username;
    private final String password;

    @Inject
    public AccountConfig(@AccountConfigSource Config config) {
        this.username = config.requireString("sealtiel.username");
        this.password = config.requireString("sealtiel.password");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
