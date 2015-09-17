package org.iatoki.judgels.sealtiel.controllers;

import org.iatoki.judgels.play.HtmlTemplate;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.controllers.security.LoggedIn;
import org.iatoki.judgels.sealtiel.forms.ClientAcquaintanceAddForm;
import org.iatoki.judgels.sealtiel.services.ClientService;
import org.iatoki.judgels.sealtiel.views.html.client.acquaintance.listAddAcquaintancesView;
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

    @Inject
    public ClientAcquaintanceController(ClientService clientService) {
        this.clientService = clientService;
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

        clientService.addClientAcquaintance(client.getJid(), clientAcquaintanceAddData.acquaintanceJid);

        return redirect(routes.ClientAcquaintanceController.index(client.getId()));
    }

    @Security.Authenticated(LoggedIn.class)
    @Transactional
    public Result removeAcquaintance(long clientId, String acquaintancJid) {
        Client client = clientService.findClientById(clientId);
        if (client == null) {
            return notFound();
        }

        clientService.removeClientAcquaintance(client.getJid(), acquaintancJid);

        return redirect(routes.ClientAcquaintanceController.index(clientId));
    }

    private Result showListAddAcquaintance(Client client, Form<ClientAcquaintanceAddForm> acquaintanceAddForm, List<Client> clients, List<Client> acquaintances) {
        HtmlTemplate template = new HtmlTemplate();

        template.setContent(listAddAcquaintancesView.render(client, acquaintanceAddForm, clients, acquaintances));
        template.setPageTitle(client.getName());

        return renderTemplate(template, client);
    }

    @Override
    protected Result renderTemplate(HtmlTemplate template, Client client) {
        template.addMainTab(Messages.get("client.acquaintance.text.acquaintances"), routes.ClientAcquaintanceController.index(client.getId()));
        template.markBreadcrumbLocation(Messages.get("client.acquaintance.text.acquaintances"), routes.ClientAcquaintanceController.index(client.getId()));

        return super.renderTemplate(template, client);
    }
}
