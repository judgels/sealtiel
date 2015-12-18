package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.play.AbstractJudgelsController;
import org.iatoki.judgels.play.template.HtmlTemplate;
import play.i18n.Messages;
import play.mvc.Result;

public abstract class AbstractSealtielController extends AbstractJudgelsController {

    @Override
    protected Result renderTemplate(HtmlTemplate template) {
        template.addSidebarMenu(Messages.get("client.text.clients"), org.iatoki.judgels.sealtiel.client.routes.ClientController.index());
        template.addSidebarMenu(Messages.get("connection.text.connection"), org.iatoki.judgels.sealtiel.connection.routes.ConnectionController.index());

        return super.renderTemplate(template);
    }
}
