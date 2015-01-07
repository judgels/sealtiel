package org.iatoki.judgels.sealtiel;

import org.apache.commons.lang3.RandomStringUtils;

public class SealtielUtilities {

    public static String generateToken(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

}
