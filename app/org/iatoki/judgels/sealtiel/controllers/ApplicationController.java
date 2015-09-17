package org.iatoki.judgels.sealtiel.controllers;

import org.iatoki.judgels.play.HtmlTemplate;
import org.iatoki.judgels.sealtiel.SealtielProperties;
import org.iatoki.judgels.sealtiel.forms.LoginForm;
import org.iatoki.judgels.sealtiel.views.html.loginView;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.mvc.Result;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named
public final class ApplicationController extends AbstractSealtielController {

    @AddCSRFToken
    public Result login() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class);

        return showLogin(loginForm);
    }

    @RequireCSRFCheck
    public Result postLogin() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();

        if (formHasErrors(loginForm)) {
            return showLogin(loginForm);
        }

        LoginForm loginData = loginForm.get();

        String username = loginData.username;
        String password = loginData.password;

        if (!SealtielProperties.getInstance().getSealtielUsername().equals(username) || !SealtielProperties.getInstance().getSealtielPassword().equals(password)) {
            loginForm.reject("auth.error.wrongCredentials");
            return showLogin(loginForm);
        }


        session().clear();
        session("username", SealtielProperties.getInstance().getSealtielUsername());
        return redirect(routes.ClientController.index());
    }

    public Result logout() {
        session().clear();
        return redirect("/");
    }

    private Result showLogin(Form<LoginForm> form) {
        HtmlTemplate template = new HtmlTemplate();

        template.setSingleColumn();
        template.setContent(loginView.render(form));
        template.setMainTitle(Messages.get("auth.text.logIn"));
        template.markBreadcrumbLocation(Messages.get("auth.text.logIn"), routes.ApplicationController.login());
        template.setPageTitle(Messages.get("auth.text.logIn"));

        return renderTemplate(template);
    }
}
