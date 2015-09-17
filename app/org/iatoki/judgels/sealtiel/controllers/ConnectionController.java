package org.iatoki.judgels.sealtiel.controllers;

import org.iatoki.judgels.play.HtmlTemplate;
import org.iatoki.judgels.sealtiel.controllers.security.LoggedIn;
import org.iatoki.judgels.sealtiel.services.impls.RabbitmqImpl;
import org.iatoki.judgels.sealtiel.views.html.connection.connectionView;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Security.Authenticated(LoggedIn.class)
@Singleton
@Named
public final class ConnectionController extends AbstractSealtielController {

    public Result index() {
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

        HtmlTemplate template = new HtmlTemplate();

        template.setContent(connectionView.render(status));
        template.setMainTitle(Messages.get("connection.text.status"));
        template.markBreadcrumbLocation(Messages.get("connection.text.connection"), routes.ConnectionController.index());
        template.setPageTitle(Messages.get("connection.text.status"));

        return renderTemplate(template);
    }
}
