package org.iatoki.judgels.sealtiel.controllers.security;

import org.iatoki.judgels.sealtiel.SealtielProperties;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public final class LoggedIn extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        if ((ctx.session().get("username") == null) || (!SealtielProperties.getInstance().getSealtielUsername().equals(ctx.session().get("username")))) {
            return null;
        }

        return ctx.session().get("username");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return forbidden();
    }
}
