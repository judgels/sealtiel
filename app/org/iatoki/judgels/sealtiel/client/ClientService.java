package org.iatoki.judgels.sealtiel.client;

import com.google.inject.ImplementedBy;
import org.iatoki.judgels.play.services.JudgelsAppClientService;

import java.util.List;

@ImplementedBy(ClientServiceImpl.class)
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
