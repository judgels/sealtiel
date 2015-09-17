package org.iatoki.judgels.sealtiel.forms;

import play.data.validation.Constraints;

public final class ClientAcquaintanceAddForm {

    @Constraints.Required
    public String acquaintanceJid;
}
