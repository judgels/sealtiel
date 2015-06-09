package org.iatoki.judgels.sealtiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.commons.InternalLink;
import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.controllers.BaseController;
import org.iatoki.judgels.commons.views.html.layouts.headingLayout;
import org.iatoki.judgels.commons.views.html.layouts.headingWithActionLayout;
import org.iatoki.judgels.commons.views.html.layouts.tabLayout;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.ClientCreateForm;
import org.iatoki.judgels.sealtiel.ClientService;
import org.iatoki.judgels.sealtiel.controllers.security.LoggedIn;
import org.iatoki.judgels.sealtiel.views.html.client.createClientView;
import org.iatoki.judgels.sealtiel.views.html.client.listClientsView;
import org.iatoki.judgels.sealtiel.views.html.client.viewClientView;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

@Security.Authenticated(LoggedIn.class)
public final class ClientController extends BaseController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Transactional(readOnly = true)
    public Result index() {
        LazyHtml content = new LazyHtml(listClientsView.render(clientService.findAllClient()));
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
        Form<ClientCreateForm> newClientForm = Form.form(ClientCreateForm.class).bindFromRequest();
        if (newClientForm.hasErrors()) {
            return showCreateClient(newClientForm);
        } else {
            ClientCreateForm data = newClientForm.get();
            clientService.createClient(data.name);
            return redirect(routes.ClientController.index());
        }

    }

    @Transactional(readOnly = true)
    public Result viewClient(long clientId) {
        Client client = clientService.findClientByClientId(clientId);
        if (client != null) {
            List<Client> acquaintances = clientService.findClientsByClientJids(client.getAcquaintances());
            LazyHtml content = new LazyHtml(viewClientView.render(client, clientService.findAllClient(), acquaintances));
            content.appendLayout(c -> tabLayout.render(ImmutableList.of(new InternalLink(Messages.get("channel.channels"), routes.ClientController.viewClient(client.getId()))), c));
            ControllerUtils.getInstance().appendSidebarLayout(content);
            ControllerUtils.getInstance().appendBreadcrumbsLayout(content, ImmutableList.of(
                  new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                  new InternalLink(Messages.get("client.view"), routes.ClientController.viewClient(client.getId()))
            ));
            ControllerUtils.getInstance().appendTemplateLayout(content, "Client - View");

            return ControllerUtils.getInstance().lazyOk(content);
        } else {
            return notFound();
        }
    }

    @Transactional
    public Result addClientAcquaintance(long clientId) {
        Client client = clientService.findClientByClientId(clientId);
        if (client != null) {
            DynamicForm form = DynamicForm.form().bindFromRequest();
            String acquaintanceChannel = form.get("acquaintance");
            clientService.addAcquaintance(client.getJid(), acquaintanceChannel);

            return redirect(routes.ClientController.viewClient(clientId));
        } else {
            return notFound();
        }
    }

    @Transactional
    public Result removeClientAcquaintance(long clientId, String acquaintanceChannel) {
        Client client = clientService.findClientByClientId(clientId);
        if (client != null) {

            clientService.removeAcquaintance(client.getJid(), acquaintanceChannel);

            return redirect(routes.ClientController.viewClient(clientId));
        } else {
            return notFound();
        }
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
