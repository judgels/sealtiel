package org.iatoki.judgels.sealtiel;

import com.google.gson.Gson;

public class GsonWrapper {

    private static Gson INSTANCE;

    public static Gson getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Gson();
        }
        return INSTANCE;
    }

}
