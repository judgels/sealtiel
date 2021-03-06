package org.iatoki.judgels.sealtiel.client.acquaintance;

import com.google.inject.ImplementedBy;

@ImplementedBy(ClientAcquaintanceServiceImpl.class)
public interface ClientAcquaintanceService {
    void addAcquaintance(String clientJid, String acquaintanceJid);

    void removeAcquaintance(String clientJid, String acquaintanceJid);

}
