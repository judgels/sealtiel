package org.iatoki.judgels.sealtiel.services;

import org.iatoki.judgels.sealtiel.Client;

import java.util.List;

public interface ClientService {

    void createClient(String name);

    List<Client> getAllClients();

    boolean existsByClientJid(String clientJid);

    Client findClientById(long clientId);

    Client findClientByJid(String clientJid);

    List<Client> findClientsByJids(List<String> clientJids);

    void downloadLib(String clientJid);

    void addClientAcquaintance(String clientJid, String acquaintance);

    void removeClientAcquaintance(String clientJid, String acquaintance);
}
