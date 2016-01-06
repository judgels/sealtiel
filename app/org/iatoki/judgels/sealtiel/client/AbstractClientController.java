package org.iatoki.judgels.sealtiel.client;

import org.iatoki.judgels.play.template.HtmlTemplate;
import org.iatoki.judgels.sealtiel.AbstractSealtielController;
import play.i18n.Messages;
import play.mvc.Result;

public abstract class AbstractClientController extends AbstractSealtielController {

    @Override
    protected HtmlTemplate getBaseHtmlTemplate() {
        HtmlTemplate htmlTemplate = super.getBaseHtmlTemplate();
        htmlTemplate.markBreadcrumbLocation(Messages.get("client.text.clients"), routes.ClientController.index());

        return htmlTemplate;
    }

    @Override
    protected Result renderTemplate(HtmlTemplate template) {
        return super.renderTemplate(template);
    }

    protected Result renderTemplate(HtmlTemplate template, Client client) {
        template.setMainTitle("#" + client.getId() + ": " + client.getName());

        template.markBreadcrumbLocation(client.getName(), routes.ClientController.editClient(client.getId()));

        return renderTemplate(template);
    }
}
