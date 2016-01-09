package org.iatoki.judgels.sealtiel.client.acquaintance;

import org.iatoki.judgels.play.template.HtmlTemplate;
import org.iatoki.judgels.sealtiel.client.AbstractClientController;
import org.iatoki.judgels.sealtiel.client.Client;
import org.iatoki.judgels.sealtiel.client.ClientService;
import org.iatoki.judgels.sealtiel.client.acquaintance.html.listAddAcquaintancesView;
import org.iatoki.judgels.sealtiel.security.LoggedIn;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

@Security.Authenticated(LoggedIn.class)
@Singleton
@Named
public final class ClientAcquaintanceController extends AbstractClientController {

    private final ClientService clientService;
    private final ClientAcquaintanceService clientAcquaintanceService;

    @Inject
    public ClientAcquaintanceController(ClientService clientService, ClientAcquaintanceService clientAcquaintanceService) {
        this.clientService = clientService;
        this.clientAcquaintanceService = clientAcquaintanceService;
    }

    @Transactional
    public Result index(long clientId) {
        Client client = clientService.findClientById(clientId);

        List<Client> clients = clientService.getAllClients();
        List<Client> acquaintances = clientService.findClientsByJids(client.getAcquaintances());

        Form<ClientAcquaintanceAddForm> acquaintanceAddForm = Form.form(ClientAcquaintanceAddForm.class);

        return showListAddAcquaintance(client, acquaintanceAddForm, clients, acquaintances);
    }

    @Transactional
    public Result postAddAcquaintance(long clientId) {
        Client client = clientService.findClientById(clientId);

        Form<ClientAcquaintanceAddForm> clientAcquaintanceAddForm = Form.form(ClientAcquaintanceAddForm.class).bindFromRequest();
        ClientAcquaintanceAddForm clientAcquaintanceAddData = clientAcquaintanceAddForm.get();

        clientAcquaintanceService.addAcquaintance(client.getJid(), clientAcquaintanceAddData.acquaintanceJid);

        return redirect(routes.ClientAcquaintanceController.index(client.getId()));
    }

    @Security.Authenticated(LoggedIn.class)
    @Transactional
    public Result removeAcquaintance(long clientId, String acquaintanceJid) {
        Client client = clientService.findClientById(clientId);
        if (client == null) {
            return notFound();
        }

        clientAcquaintanceService.removeAcquaintance(client.getJid(), acquaintanceJid);

        return redirect(routes.ClientAcquaintanceController.index(clientId));
    }

    private Result showListAddAcquaintance(Client client, Form<ClientAcquaintanceAddForm> acquaintanceAddForm, List<Client> clients, List<Client> acquaintances) {
        HtmlTemplate template = getBaseHtmlTemplate(client);

        template.setContent(listAddAcquaintancesView.render(client, acquaintanceAddForm, clients, acquaintances));
        template.setPageTitle(client.getName());

        return renderTemplate(template);
    }

    @Override
    protected HtmlTemplate getBaseHtmlTemplate(Client client) {
        HtmlTemplate template = super.getBaseHtmlTemplate(client);

        template.addMainTab(Messages.get("client.acquaintance.text.acquaintances"), routes.ClientAcquaintanceController.index(client.getId()));
        template.markBreadcrumbLocation(Messages.get("client.acquaintance.text.acquaintances"), routes.ClientAcquaintanceController.index(client.getId()));

        return template;
    }
}
