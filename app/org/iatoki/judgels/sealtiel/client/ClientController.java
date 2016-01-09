package org.iatoki.judgels.sealtiel.client;

import org.iatoki.judgels.play.IdentityUtils;
import org.iatoki.judgels.play.template.HtmlTemplate;
import org.iatoki.judgels.sealtiel.security.LoggedIn;
import org.iatoki.judgels.sealtiel.client.html.createClientView;
import org.iatoki.judgels.sealtiel.client.html.editClientView;
import org.iatoki.judgels.sealtiel.client.html.listClientsView;
import play.data.Form;
import play.db.jpa.Transactional;
import play.filters.csrf.AddCSRFToken;
import play.filters.csrf.RequireCSRFCheck;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;

@Singleton
@Named
public final class ClientController extends AbstractClientController {

    private final ClientService clientService;

    @Inject
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Transactional(readOnly = true)
    public Result index() {
        if (session("username") == null) {
            return redirect(org.iatoki.judgels.sealtiel.account.routes.AccountController.login());
        }

        List<Client> clients = clientService.getAllClients();

        return showListClients(clients);
    }

    @Security.Authenticated(LoggedIn.class)
    @AddCSRFToken
    public Result createClient() {
        Form<ClientForm> clientCreateForm = Form.form(ClientForm.class);

        return showCreateClient(clientCreateForm);
    }

    @Security.Authenticated(LoggedIn.class)
    @RequireCSRFCheck
    @Transactional
    public Result postCreateClient() {
        Form<ClientForm> clientCreateForm = Form.form(ClientForm.class).bindFromRequest();

        if (formHasErrors(clientCreateForm)) {
            return showCreateClient(clientCreateForm);
        }

        ClientForm clientCreateData = clientCreateForm.get();
        Client client = clientService.createClient(clientCreateData.name, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());

        return redirect(routes.ClientController.enterClient(client.getId()));
    }

    @Security.Authenticated(LoggedIn.class)
    @AddCSRFToken
    @Transactional
    public Result editClient(long clientId) {
        Client client = clientService.findClientById(clientId);

        ClientForm clientEditData = new ClientForm();
        clientEditData.name = client.getName();

        Form<ClientForm> clientEditForm = Form.form(ClientForm.class).fill(clientEditData);

        return showEditClient(client, clientEditForm);
    }

    @Security.Authenticated(LoggedIn.class)
    @RequireCSRFCheck
    @Transactional
    public Result postEditClient(long clientId) {
        Client client = clientService.findClientById(clientId);

        Form<ClientForm> clientEditForm = Form.form(ClientForm.class).bindFromRequest();
        if (formHasErrors(clientEditForm)) {
            return showEditClient(client, clientEditForm);
        }

        ClientForm clientEditData = clientEditForm.get();
        clientService.updateClient(client.getJid(), clientEditData.name, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());

        return redirect(routes.ClientController.index());
    }

    public Result enterClient(long clientId) {
        return redirect(org.iatoki.judgels.sealtiel.client.acquaintance.routes.ClientAcquaintanceController.index(clientId));
    }

    private Result showListClients(List<Client> clients) {
        HtmlTemplate template = getBaseHtmlTemplate();

        template.setContent(listClientsView.render(clients));
        template.setMainTitle(Messages.get("client.text.list"));
        template.addMainButton(Messages.get("client.button.new"), routes.ClientController.createClient());

        return renderTemplate(template);
    }

    private Result showCreateClient(Form<ClientForm> clientCreateForm) {
        HtmlTemplate template = getBaseHtmlTemplate();

        template.setContent(createClientView.render(clientCreateForm));
        template.setMainTitle(Messages.get("client.text.new"));
        template.markBreadcrumbLocation(Messages.get("commons.text.new"), routes.ClientController.createClient());
        template.setPageTitle(Messages.get("client.text.new"));

        return renderTemplate(template);
    }

    private Result showEditClient(Client client, Form<ClientForm> clientEditForm) {
        HtmlTemplate template = getBaseHtmlTemplate(client);

        template.setContent(editClientView.render(client, clientEditForm));
        template.markBreadcrumbLocation(Messages.get("commons.text.edit"), routes.ClientController.editClient(client.getId()));
        template.setPageTitle(client.getName());

        return renderTemplate(template);
    }
}
