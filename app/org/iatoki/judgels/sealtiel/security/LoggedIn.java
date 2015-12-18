package org.iatoki.judgels.sealtiel.security;

import org.iatoki.judgels.sealtiel.account.AccountConfig;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;

public final class LoggedIn extends Security.Authenticator {

    @Inject
    private AccountConfig config;

    @Override
    public String getUsername(Context ctx) {
        if ((ctx.session().get("username") == null) || (!config.getUsername().equals(ctx.session().get("username")))) {
            return null;
        }

        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return forbidden();
    }
}
