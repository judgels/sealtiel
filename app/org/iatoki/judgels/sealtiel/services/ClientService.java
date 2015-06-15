package org.iatoki.judgels.sealtiel.services;

import org.iatoki.judgels.sealtiel.Client;

import java.util.List;

public interface ClientService {
    void createClient(String name);

    List<Client> findAllClient();

    boolean existByClientJid(String clientJid);

    Client findClientByClientId(long clientId);

    Client findClientByClientJid(String clientJid);

    List<Client> findClientsByClientJids(List<String> clientJids);

    void downloadLib(String clientJid);

    void addAcquaintance(String clientJid, String acquaintance);

    void removeAcquaintance(String clientJid, String acquaintance);

}
