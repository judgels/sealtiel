package org.iatoki.judgels.sealtiel.controllers;

import org.iatoki.judgels.play.HtmlTemplate;
import org.iatoki.judgels.sealtiel.Client;
import play.i18n.Messages;
import play.mvc.Result;

public abstract class AbstractClientController extends AbstractSealtielController {

    @Override
    protected Result renderTemplate(HtmlTemplate template) {
        template.markBreadcrumbLocation(Messages.get("client.text.clients"), routes.ClientController.index());

        return super.renderTemplate(template);
    }

    protected Result renderTemplate(HtmlTemplate template, Client client) {
        template.setMainTitle("#" + client.getId() + ": " + client.getName());

        template.markBreadcrumbLocation(client.getName(), routes.ClientController.editClient(client.getId()));

        return renderTemplate(template);
    }
}
