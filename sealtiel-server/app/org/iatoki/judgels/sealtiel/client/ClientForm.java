package org.iatoki.judgels.sealtiel.client;

import play.data.validation.Constraints;

public final class ClientForm {

    @Constraints.Required
    public String name;
}
