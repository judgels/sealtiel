package org.iatoki.judgels.sealtiel.controllers;

import org.iatoki.judgels.play.HtmlTemplate;
import org.iatoki.judgels.play.controllers.AbstractJudgelsController;
import play.i18n.Messages;
import play.mvc.Result;

public abstract class AbstractSealtielController extends AbstractJudgelsController {

    @Override
    protected Result renderTemplate(HtmlTemplate template) {
        template.addSidebarMenu(Messages.get("client.text.clients"), routes.ClientController.index());
        template.addSidebarMenu(Messages.get("connection.text.connection"), routes.ConnectionController.index());

        return super.renderTemplate(template);
    }
}
