package org.iatoki.judgels.sealtiel.controllers.forms;

import play.data.validation.Constraints;

public final class ClientCreateForm {
    @Constraints.Required
    public String name;
}
