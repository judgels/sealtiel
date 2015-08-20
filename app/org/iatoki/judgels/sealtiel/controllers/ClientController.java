package org.iatoki.judgels.sealtiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.play.InternalLink;
import org.iatoki.judgels.play.LazyHtml;
import org.iatoki.judgels.play.controllers.AbstractJudgelsController;
import org.iatoki.judgels.play.views.html.layouts.headingLayout;
import org.iatoki.judgels.play.views.html.layouts.headingWithActionLayout;
import org.iatoki.judgels.play.views.html.layouts.tabLayout;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.forms.ClientCreateForm;
import org.iatoki.judgels.sealtiel.services.ClientService;
import org.iatoki.judgels.sealtiel.controllers.securities.LoggedIn;
import org.iatoki.judgels.sealtiel.views.html.client.createClientView;
import org.iatoki.judgels.sealtiel.views.html.client.listClientsView;
import org.iatoki.judgels.sealtiel.views.html.client.viewClientView;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Security;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Security.Authenticated(LoggedIn.class)
@Named
public final class ClientController extends AbstractJudgelsController {

    private final ClientService clientService;

    @Inject
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Transactional(readOnly = true)
    public Result index() {
        LazyHtml content = new LazyHtml(listClientsView.render(clientService.getAllClients()));
        content.appendLayout(c -> headingWithActionLayout.render(Messages.get("client.list"), new InternalLink(Messages.get("commons.create"), routes.ClientController.createClient()), c));
        ControllerUtils.getInstance().appendSidebarLayout(content);
        ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
              new InternalLink(Messages.get("client.clients"), routes.ClientController.index())
        ));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Clients");

        return ControllerUtils.getInstance().lazyOk(content);
    }

    public Result createClient() {
        return showCreateClient(Form.form(ClientCreateForm.class));
    }

    @Transactional
    public Result postCreateClient() {
        Form<ClientCreateForm> clientCreateForm = Form.form(ClientCreateForm.class).bindFromRequest();
        if (formHasErrors(clientCreateForm)) {
            return showCreateClient(clientCreateForm);
        }

        ClientCreateForm clientCreateData = clientCreateForm.get();
        clientService.createClient(clientCreateData.name);

        return redirect(routes.ClientController.index());
    }

    @Transactional(readOnly = true)
    public Result viewClient(long clientId) {
        Client client = clientService.findClientById(clientId);
        if (client == null) {
            return notFound();
        }

        List<Client> acquaintances = clientService.findClientsByJids(client.getAcquaintances());

        LazyHtml content = new LazyHtml(viewClientView.render(client, clientService.getAllClients(), acquaintances));
        content.appendLayout(c -> tabLayout.render(ImmutableList.of(new InternalLink(Messages.get("acquaintance.acquaintances"), routes.ClientController.viewClient(client.getId()))), c));
        ControllerUtils.getInstance().appendSidebarLayout(content);
        ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
              new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
              new InternalLink(Messages.get("client.view"), routes.ClientController.viewClient(client.getId()))
        ));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Client - View");

        return ControllerUtils.getInstance().lazyOk(content);
    }

    @Transactional
    public Result addClientAcquaintance(long clientId) {
        Client client = clientService.findClientById(clientId);
        if (client == null) {
            return notFound();
        }

        DynamicForm dForm = DynamicForm.form().bindFromRequest();
        String acquaintanceChannel = dForm.get("acquaintance");
        clientService.addClientAcquaintance(client.getJid(), acquaintanceChannel);

        return redirect(routes.ClientController.viewClient(clientId));
    }

    @Transactional
    public Result removeClientAcquaintance(long clientId, String acquaintanceChannel) {
        Client client = clientService.findClientById(clientId);
        if (client == null) {
            return notFound();
        }

        clientService.removeClientAcquaintance(client.getJid(), acquaintanceChannel);

        return redirect(routes.ClientController.viewClient(clientId));
    }

    private Result showCreateClient(Form<ClientCreateForm> form) {
        LazyHtml content = new LazyHtml(createClientView.render(form));
        content.appendLayout(c -> headingLayout.render(Messages.get("client.create"), c));
        ControllerUtils.getInstance().appendSidebarLayout(content);
        ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
              new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
              new InternalLink(Messages.get("client.create"), routes.ClientController.createClient())
        ));
        ControllerUtils.getInstance().appendTemplateLayout(content, "Client - Create");

        return ControllerUtils.getInstance().lazyOk(content);
    }
}
