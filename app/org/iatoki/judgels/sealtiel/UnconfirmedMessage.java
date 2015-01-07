package org.iatoki.judgels.sealtiel;

import java.util.HashMap;

public class UnconfirmedMessage extends HashMap<Long, Long> {
    private static UnconfirmedMessage INSTANCE;

    public static UnconfirmedMessage getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnconfirmedMessage();
        }
        return INSTANCE;
    }
}
