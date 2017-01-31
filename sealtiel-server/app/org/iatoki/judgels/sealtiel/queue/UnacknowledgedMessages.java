package org.iatoki.judgels.sealtiel.queue;

import java.util.HashMap;

public final class UnacknowledgedMessages extends HashMap<Long, Long> {

    private static UnacknowledgedMessages INSTANCE = new UnacknowledgedMessages();

    private UnacknowledgedMessages() {
        // prevent instantiation
    }

    public static UnacknowledgedMessages getInstance() {
        return INSTANCE;
    }
}
