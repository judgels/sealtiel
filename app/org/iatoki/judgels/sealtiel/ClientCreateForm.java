package org.iatoki.judgels.sealtiel;

import play.data.validation.Constraints;

public class ClientCreateForm {
    @Constraints.Required
    public String appName;

    @Constraints.Required
    public String adminName;

    @Constraints.Required
    @Constraints.Email
    public String adminEmail;
}
