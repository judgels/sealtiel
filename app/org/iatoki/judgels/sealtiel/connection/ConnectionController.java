package org.iatoki.judgels.sealtiel.connection;

import org.iatoki.judgels.play.template.HtmlTemplate;
import org.iatoki.judgels.sealtiel.AbstractSealtielController;
import org.iatoki.judgels.sealtiel.connection.html.connectionView;
import org.iatoki.judgels.sealtiel.queue.rabbitmq.RabbitMQService;
import org.iatoki.judgels.sealtiel.security.LoggedIn;
import org.iatoki.judgels.sealtiel.queue.QueueService;
import org.iatoki.judgels.sealtiel.queue.rabbitmq.RabbitMQ;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Security.Authenticated(LoggedIn.class)
@Singleton
@Named
public final class ConnectionController extends AbstractSealtielController {

    @Inject
    @RabbitMQService
    private QueueService queueService;

    public Result index() {
        RabbitMQ rabbitMQ = (RabbitMQ) queueService;
        boolean status = rabbitMQ.isConnected();
        if (!status) {
            try {
                rabbitMQ.createConnection();
            } catch (IOException | TimeoutException e) {
                // do nothing
            }
            status = rabbitMQ.isConnected();
        }

        HtmlTemplate template = new HtmlTemplate();

        template.setContent(connectionView.render(status));
        template.setMainTitle(Messages.get("connection.text.status"));
        template.markBreadcrumbLocation(Messages.get("connection.text.connection"), routes.ConnectionController.index());
        template.setPageTitle(Messages.get("connection.text.status"));

        return renderTemplate(template);
    }
}
