package org.iatoki.judgels.sealtiel.account;

import play.data.validation.Constraints;

public class LoginForm {

    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
