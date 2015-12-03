package org.iatoki.judgels.sealtiel.client.acquaintance;

public interface ClientAcquaintanceService {
    void addAcquaintance(String clientJid, String acquaintance);

    void removeAcquaintance(String clientJid, String acquaintance);

}
