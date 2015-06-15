package org.iatoki.judgels.sealtiel.controllers.securities;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public final class LoggedIn extends Security.Authenticator {

    @Override
        public String getUsername(Context ctx) {
        if ((ctx.session().get("username")!=null) && ("sealtiel".equals(ctx.session().get("username")))) {
            return ctx.session().get("username");
        } else {
            return null;
        }
        }

        @Override
        public Result onUnauthorized(Context ctx) {
                return forbidden();
        }

}
