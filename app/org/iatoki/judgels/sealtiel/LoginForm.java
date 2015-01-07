package org.iatoki.judgels.sealtiel;

import play.data.validation.Constraints;

public class LoginForm {
    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    public String validate() {
        if (("sealtiel".equals(username)) && ("rahasiasealtiel".equals(password))) {
            return null;
        } else {
            return "Username atau sandi salah.";
        }
    }
}
