package org.iatoki.judgels.sealtiel;

import java.util.HashMap;

public final class UnconfirmedMessage extends HashMap<Long, Long> {
    private static UnconfirmedMessage INSTANCE = new UnconfirmedMessage();

    private UnconfirmedMessage() {

    }

    public static UnconfirmedMessage getInstance() {
        return INSTANCE;
    }
}
