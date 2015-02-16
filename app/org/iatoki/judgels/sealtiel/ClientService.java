package org.iatoki.judgels.sealtiel;

import java.util.List;

public interface ClientService {
    void createClient(String appName, String adminName, String adminEmail);

    List<Client> findAllClient();

    Client findClientByClientId(long clientId);

    Client findClientByClientJid(String clientJid);

    List<Client> findClientsByClientJids(List<String> clientJids);

    void downloadLib(String clientJid);

    void addAcquaintance(String clientJid, String acquaintance);

    void removeAcquaintance(String clientJid, String acquaintance);

}
