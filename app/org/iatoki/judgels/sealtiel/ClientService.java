package org.iatoki.judgels.sealtiel;

import java.util.List;

public interface ClientService {
    void createClient(String appName, String adminName, String adminEmail);

    List<Client> findAllClient();

    Client findClientById(long clientId);

    Client findClientByClientId(String clientId);

    Client findClientByChannel(String channel);

    List<Client> findClientsByClientChannels(List<String> clientChannels);

    void downloadLib(String clientId);

    void addAcquaintance(String clientId, String acquaintance);

    void removeAcquaintance(String clientId, String acquaintance);

}
