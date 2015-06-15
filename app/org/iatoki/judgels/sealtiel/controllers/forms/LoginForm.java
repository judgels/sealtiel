package org.iatoki.judgels.sealtiel.controllers.forms;

import org.iatoki.judgels.sealtiel.SealtielProperties;
import play.Play;
import play.data.validation.Constraints;

public class LoginForm {
    @Constraints.Required
    public String username;

    @Constraints.Required
    public String password;

    public String validate() {
        if ((SealtielProperties.getInstance().getSealtielUsername().equals(username)) && (SealtielProperties.getInstance().getSealtielPassword().equals(password))) {
            return null;
        } else {
            return "Username atau sandi salah.";
        }
    }
}
