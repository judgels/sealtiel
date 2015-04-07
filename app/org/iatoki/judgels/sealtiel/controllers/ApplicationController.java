package org.iatoki.judgels.sealtiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.commons.InternalLink;
import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.views.html.layouts.centerLayout;
import org.iatoki.judgels.commons.views.html.layouts.sidebarWithoutProfileLayout;
import org.iatoki.judgels.sealtiel.LoginForm;
import org.iatoki.judgels.sealtiel.RabbitmqConnection;
import org.iatoki.judgels.sealtiel.views.html.connectionView;
import org.iatoki.judgels.sealtiel.views.html.indexView;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;

public class ApplicationController extends Controller {

    public ApplicationController() {

    }

    private Result showLogin(Form<LoginForm> form) {
        LazyHtml content = new LazyHtml(indexView.render(form));
        content.appendLayout(c -> centerLayout.render(c));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Login");

        return ControllerUtils.getInstance().lazyOk(content);
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

    public Result checkRabbitmqConnection() {
        RabbitmqConnection rabbitmqConnection = RabbitmqConnection.getInstance();
        boolean status = rabbitmqConnection.isConnected();

        LazyHtml content = new LazyHtml(connectionView.render(status));
        content.appendLayout(c -> sidebarWithoutProfileLayout.render(ImmutableList.of(
                        new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                        new InternalLink(Messages.get("system.connection"), routes.ApplicationController.checkRabbitmqConnection())
                ), c)
        );
        ControllerUtils.getInstance().appendTemplateLayout(content, "System - Rabbitmq");

        return ControllerUtils.getInstance().lazyOk(content);
    }
}
