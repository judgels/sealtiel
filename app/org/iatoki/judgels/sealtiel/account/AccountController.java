package org.iatoki.judgels.sealtiel.account;

import org.iatoki.judgels.play.template.HtmlTemplate;
import org.iatoki.judgels.sealtiel.AbstractSealtielController;
import org.iatoki.judgels.sealtiel.account.html.loginView;
import play.data.Form;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class AccountController extends AbstractSealtielController {

    @Inject
    private AccountConfig config;

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

        if (!config.getUsername().equals(username) || !config.getPassword().equals(password)) {
            loginForm.reject("auth.error.wrongCredentials");
            return showLogin(loginForm);
        }


        session().clear();
        session("username", config.getUsername());
        return redirect(org.iatoki.judgels.sealtiel.client.routes.ClientController.index());
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
        template.markBreadcrumbLocation(Messages.get("auth.text.logIn"), routes.AccountController.login());
        template.setPageTitle(Messages.get("auth.text.logIn"));

        return renderTemplate(template);
    }
}
