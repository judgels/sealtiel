package org.iatoki.judgels.sealtiel.client;

import org.iatoki.judgels.play.template.HtmlTemplate;
import org.iatoki.judgels.sealtiel.AbstractSealtielController;
import play.i18n.Messages;

public abstract class AbstractClientController extends AbstractSealtielController {

    @Override
    protected HtmlTemplate getBaseHtmlTemplate() {
        HtmlTemplate template = super.getBaseHtmlTemplate();

        template.markBreadcrumbLocation(Messages.get("client.text.clients"), routes.ClientController.index());

        return template;
    }

    protected HtmlTemplate getBaseHtmlTemplate(Client client) {
        HtmlTemplate template = getBaseHtmlTemplate();

        template.setMainTitle("#" + client.getId() + ": " + client.getName());
        template.markBreadcrumbLocation(client.getName(), routes.ClientController.editClient(client.getId()));

        return template;
    }
}
