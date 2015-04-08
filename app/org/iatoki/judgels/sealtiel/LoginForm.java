package org.iatoki.judgels.sealtiel;

import play.Play;
import play.data.validation.Constraints;

public class LoginForm {
    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    public String validate() {
        if ((Play.application().configuration().getString("sealtiel.username").equals(username)) && (Play.application().configuration().getString("sealtiel.password").equals(password))) {
            return null;
        } else {
            return "Username atau sandi salah.";
        }
    }
}
