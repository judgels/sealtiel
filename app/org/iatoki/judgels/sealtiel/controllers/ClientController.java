package org.iatoki.judgels.sealtiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.commons.InternalLink;
import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.views.html.layouts.baseLayout;
import org.iatoki.judgels.commons.views.html.layouts.breadcrumbsLayout;
import org.iatoki.judgels.commons.views.html.layouts.headerFooterLayout;
import org.iatoki.judgels.commons.views.html.layouts.headingLayout;
import org.iatoki.judgels.commons.views.html.layouts.headingWithActionLayout;
import org.iatoki.judgels.commons.views.html.layouts.leftSidebarWithoutProfileLayout;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.ClientCreateForm;
import org.iatoki.judgels.sealtiel.ClientService;
import org.iatoki.judgels.sealtiel.LoggedIn;
import org.iatoki.judgels.sealtiel.views.html.client.createView;
import org.iatoki.judgels.sealtiel.views.html.client.listView;
import org.iatoki.judgels.sealtiel.views.html.client.viewView;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Arrays;
import java.util.List;

@Security.Authenticated(LoggedIn.class)
public class ClientController extends Controller {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @Transactional(readOnly = true)
    public Result index() {
        LazyHtml content = new LazyHtml(listView.render(clientService.findAllClient()));
        content.appendLayout(c -> headingWithActionLayout.render(Messages.get("client.list"), new InternalLink(Messages.get("client.create"), routes.ClientController.create()), c));
        content.appendLayout(c -> breadcrumbsLayout.render(ImmutableList.of(
                new InternalLink(Messages.get("client.clients"), routes.ClientController.index())
        ), c));
        appendTemplateLayout(content);

        return lazyOk(content);
    }

    private Result showCreate(Form<ClientCreateForm> form) {
        LazyHtml content = new LazyHtml(createView.render(form));
        content.appendLayout(c -> headingLayout.render(Messages.get("client.create"), c));
        content.appendLayout(c -> breadcrumbsLayout.render(ImmutableList.of(
                new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                new InternalLink(Messages.get("client.create"), routes.ClientController.create())
        ), c));
        appendTemplateLayout(content);

        return lazyOk(content);
    }

    public Result create() {
        return showCreate(Form.form(ClientCreateForm.class));
    }

    @Transactional
    public Result doCreate() {
        Form<ClientCreateForm> newClientForm = Form.form(ClientCreateForm.class).bindFromRequest();
        if (newClientForm.hasErrors()) {
            return showCreate(newClientForm);
        } else {
            ClientCreateForm data = newClientForm.get();
            clientService.createClient(data.appName, data.adminName, data.adminEmail);
            return redirect(routes.ClientController.index());
        }

    }

    @Transactional(readOnly = true)
    public Result view(long clientId) {
        Client client = clientService.findClientById(clientId);
        if (client != null) {
            List<Client> acquaintances = clientService.findClientsByClientChannels(Arrays.asList(client.getAcquaintances().split(",")));
            LazyHtml content = new LazyHtml(viewView.render(client, clientService.findAllClient(), acquaintances));
            content.appendLayout(c -> headingLayout.render(Messages.get("client.view"), c));
            content.appendLayout(c -> breadcrumbsLayout.render(ImmutableList.of(
                    new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                    new InternalLink(Messages.get("client.view"), routes.ClientController.view(clientId))
            ), c));
            appendTemplateLayout(content);

            return lazyOk(content);
        } else {
            return notFound();
        }
    }

    @Transactional
    public Result addAcquaintance(long clientId) {
        Client client = clientService.findClientById(clientId);
        if (client != null) {
            DynamicForm form = DynamicForm.form().bindFromRequest();
            String acquaintanceChannel = form.get("acquaintance");
            clientService.addAcquaintance(client.getClientId(), acquaintanceChannel);

            return redirect(routes.ClientController.view(clientId));
        } else {
            return notFound();
        }
    }

    @Transactional
    public Result removeAcquaintance(long clientId, String acquaintanceChannel) {
        Client client = clientService.findClientById(clientId);
        if (client != null) {

            clientService.removeAcquaintance(client.getClientId(), acquaintanceChannel);

            return redirect(routes.ClientController.view(clientId));
        } else {
            return notFound();
        }
    }

    private void appendTemplateLayout(LazyHtml content) {
        content.appendLayout(c -> leftSidebarWithoutProfileLayout.render(ImmutableList.of(
                        new InternalLink(Messages.get("client.clients"), routes.ClientController.index())
                ), c)
        );

        content.appendLayout(c -> headerFooterLayout.render(c));
        content.appendLayout(c -> baseLayout.render("TODO", c));
    }

    private Result lazyOk(LazyHtml content) {
        return getResult(content, Http.Status.OK);
    }

    private Result getResult(LazyHtml content, int statusCode) {
        switch (statusCode) {
            case Http.Status.OK:
                return ok(content.render(0));
            case Http.Status.NOT_FOUND:
                return notFound(content.render(0));
            default:
                return badRequest(content.render(0));
        }
    }
}
