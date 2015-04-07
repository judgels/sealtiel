package org.iatoki.judgels.sealtiel.controllers;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.commons.InternalLink;
import org.iatoki.judgels.commons.LazyHtml;
import org.iatoki.judgels.commons.controllers.AbstractControllerUtils;
import org.iatoki.judgels.commons.views.html.layouts.sidebarWithoutProfileLayout;
import play.i18n.Messages;

public final class ControllerUtils extends AbstractControllerUtils {

    private static final ControllerUtils INSTANCE = new ControllerUtils();

    private ControllerUtils() {

    }

    @Override
    public void appendSidebarLayout(LazyHtml content) {
        content.appendLayout(c -> sidebarWithoutProfileLayout.render(ImmutableList.of(
                    new InternalLink(Messages.get("client.clients"), routes.ClientController.index()),
                    new InternalLink(Messages.get("system.connection"), routes.ApplicationController.checkRabbitmqConnection())
              ), c)
        );
    }

    public static final ControllerUtils getInstance() {
        return INSTANCE;
    }
}
