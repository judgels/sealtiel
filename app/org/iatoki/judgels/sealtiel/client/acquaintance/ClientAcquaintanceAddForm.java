package org.iatoki.judgels.sealtiel.client.acquaintance;

import play.data.validation.Constraints;

public final class ClientAcquaintanceAddForm {

    @Constraints.Required
    public String acquaintanceJid;
}
