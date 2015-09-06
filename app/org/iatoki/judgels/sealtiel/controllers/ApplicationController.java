package org.iatoki.judgels.sealtiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.play.InternalLink;
import org.iatoki.judgels.play.LazyHtml;
import org.iatoki.judgels.play.controllers.AbstractJudgelsController;
import org.iatoki.judgels.play.views.html.layouts.centerLayout;
import org.iatoki.judgels.sealtiel.forms.LoginForm;
import org.iatoki.judgels.sealtiel.services.impls.RabbitmqImpl;
import org.iatoki.judgels.sealtiel.views.html.connection.connectionView;
import org.iatoki.judgels.sealtiel.views.html.indexView;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;

import javax.inject.Named;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Named
public final class ApplicationController extends AbstractJudgelsController {

    public Result index() {
        if (session("username") != null) {
            return redirect(routes.ClientController.index());
        }

        return showLogin(Form.form(LoginForm.class));
    }

    public Result login() {
        Form<LoginForm> loginForm = Form.form(LoginForm.class).bindFromRequest();
        if (formHasErrors(loginForm)) {
            return showLogin(loginForm);
        }

        session().clear();
        session("username", "sealtiel");
        return redirect(routes.ClientController.index());
    }

    public Result logout() {
        session().clear();
        return redirect("/");
    }

    public Result checkRabbitmqConnection() {
        RabbitmqImpl rabbitmqImpl = RabbitmqImpl.getInstance();
        boolean status = rabbitmqImpl.isConnected();
        if (!status) {
            try {
                rabbitmqImpl.createConnection();
            } catch (IOException | TimeoutException e) {
                // do nothing
            }
            status = rabbitmqImpl.isConnected();
        }

        LazyHtml content = new LazyHtml(connectionView.render(status));
        SealtielControllerUtils.getInstance().appendSidebarLayout(content);
        SealtielControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
                new InternalLink(Messages.get("connection.connection"), routes.ApplicationController.checkRabbitmqConnection())
        ));
        SealtielControllerUtils.getInstance().appendTemplateLayout(content, "System - Rabbitmq");

        return SealtielControllerUtils.getInstance().lazyOk(content);
    }

    private Result showLogin(Form<LoginForm> form) {
        LazyHtml content = new LazyHtml(indexView.render(form));
        content.appendLayout(c -> centerLayout.render(c));
        SealtielControllerUtils.getInstance().appendTemplateLayout(content, "Login");

        return SealtielControllerUtils.getInstance().lazyOk(content);
    }
}
