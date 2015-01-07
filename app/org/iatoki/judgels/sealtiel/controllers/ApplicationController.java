package org.iatoki.judgels.sealtiel.controllers;

import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.views.html.layouts.baseLayout;
import org.iatoki.judgels.commons.views.html.layouts.headerFooterLayout;
import org.iatoki.judgels.commons.views.html.layouts.noSidebarLayout;
import org.iatoki.judgels.sealtiel.LoginForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import org.iatoki.judgels.sealtiel.views.html.indexView;

public class ApplicationController extends Controller {

    public ApplicationController() {

    }

    private Result showLogin(Form<LoginForm> form) {
        LazyHtml content = new LazyHtml(indexView.render(form));
        content.appendLayout(c -> noSidebarLayout.render(c));
        content.appendLayout(c -> headerFooterLayout.render(c));
        content.appendLayout(c -> baseLayout.render("TODO", c));

        return lazyOk(content);
    }

    public Result index() {
        if (session("username")!=null) {
            return redirect(routes.ClientController.index());
        } else {
            return showLogin(Form.form(LoginForm.class));
        }
    }

    public Result login() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return showLogin(loginForm);
        } else {
            session().clear();
            session("username", "sealtiel");
            return redirect(routes.ClientController.index());
        }
    }

    public Result logout() {
        session().clear();
        return redirect("/");
    }

    private Result lazyOk(LazyHtml content) {
        return getResult(content, Http.Status.OK);
    }

    private Result getResult(LazyHtml content, int statusCode) {
        switch (statusCode) {
            case Http.Status.OK:
                return ok(content.render(0));
            case Http.Status.NOT_FOUND:
                return notFound(content.render(0));
            default:
                return badRequest(content.render(0));
        }
    }
}
