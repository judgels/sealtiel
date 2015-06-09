package org.iatoki.judgels.sealtiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.commons.InternalLink;
import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.controllers.AbstractControllerUtils;
import org.iatoki.judgels.commons.views.html.layouts.sidebarLayout;
import org.iatoki.judgels.commons.views.html.layouts.menusView;
import play.i18n.Messages;

public final class ControllerUtils extends AbstractControllerUtils {

    private static final ControllerUtils INSTANCE = new ControllerUtils();

    private ControllerUtils() {

    }

    @Override
    public void appendSidebarLayout(LazyHtml content) {
        LazyHtml sidebarContent = new LazyHtml(menusView.render(ImmutableList.of(
                    new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                    new InternalLink(Messages.get("connection.connection"), routes.ApplicationController.checkRabbitmqConnection())
              )
        ));
        content.appendLayout(c -> sidebarLayout.render(sidebarContent.render(), c));
    }

    public static ControllerUtils getInstance() {
        return INSTANCE;
    }
}
