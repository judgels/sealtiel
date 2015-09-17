package org.iatoki.judgels.sealtiel.services;

import org.iatoki.judgels.play.services.JudgelsAppClientService;
import org.iatoki.judgels.sealtiel.Client;

import java.util.List;

public interface ClientService extends JudgelsAppClientService {

    Client createClient(String name, String userJid, String ipAddress);

    void updateClient(String clientJid, String name, String userJid, String ipAddress);

    List<Client> getAllClients();

    Client findClientById(long clientId);

    Client findClientByJid(String clientJid);

    List<Client> findClientsByJids(List<String> clientJids);

    void addClientAcquaintance(String clientJid, String acquaintanceJid);

    void removeClientAcquaintance(String clientJid, String acquaintanceJid);
}
