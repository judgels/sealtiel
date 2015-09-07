package org.iatoki.judgels.sealtiel.services;

import org.iatoki.judgels.play.services.JudgelsAppClientService;
import org.iatoki.judgels.sealtiel.Client;

import java.util.List;

public interface ClientService extends JudgelsAppClientService {

    void createClient(String name);

    List<Client> getAllClients();

    Client findClientById(long clientId);

    Client findClientByJid(String clientJid);

    List<Client> findClientsByJids(List<String> clientJids);

    void downloadLib(String clientJid);

    void addClientAcquaintance(String clientJid, String acquaintance);

    void removeClientAcquaintance(String clientJid, String acquaintance);
}
