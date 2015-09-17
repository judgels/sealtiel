package org.iatoki.judgels.sealtiel.forms;

import play.data.validation.Constraints;

public final class ClientForm {

    @Constraints.Required
    public String name;
}
