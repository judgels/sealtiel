package org.iatoki.judgels.sealtiel.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.iatoki.judgels.play.JudgelsPlayUtils;
import org.iatoki.judgels.sealtiel.client.acquaintance.ClientAcquaintanceDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.UUID;

@Singleton
public final class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;
    private final ClientAcquaintanceDao clientAcquaintanceDao;

    @Inject
    public ClientServiceImpl(ClientDao clientDao, ClientAcquaintanceDao clientAcquaintanceDao) {
        this.clientDao = clientDao;
        this.clientAcquaintanceDao = clientAcquaintanceDao;
    }

    @Override
    public Client findClientById(long clientId) {
        ClientModel clientModel = clientDao.findById(clientId);
        return createClientFromModel(clientModel);
    }

    @Override
    public Client createClient(String name, String userJid, String ipAddress) {
        ClientModel clientModel = new ClientModel();
        clientModel.name = name;
        clientModel.secret = JudgelsPlayUtils.hashMD5(UUID.randomUUID().toString());

        clientDao.persist(clientModel, userJid, ipAddress);

        return createClientFromModel(clientModel);
    }

    @Override
    public void updateClient(String clientJid, String name, String userJid, String ipAddress) {
        ClientModel clientModel = clientDao.findByJid(clientJid);
        clientModel.name = name;

        clientDao.edit(clientModel, userJid, ipAddress);
    }

    @Override
    public List<Client> getAllClients() {
        ImmutableList.Builder<Client> result = ImmutableList.builder();
        List<ClientModel> clientModels = clientDao.getAll();
        return Lists.transform(clientModels, this::createClientFromModel);
    }

    @Override
    public boolean clientExistsByJid(String clientJid) {
        return clientDao.existsByJid(clientJid);
    }

    @Override
    public Client findClientByJid(String clientJid) {
        ClientModel clientModel = clientDao.findByJid(clientJid);

        return createClientFromModel(clientModel);
    }

    @Override
    public List<Client> findClientsByJids(List<String> clientJids) {
        ImmutableList.Builder<Client> result = ImmutableList.builder();
        List<ClientModel> clientModels = clientDao.getByJids(clientJids);

        for (ClientModel clientModel : clientModels) {
            result.add(createClientFromModel(clientModel));
        }

        return result.build();
    }

    private Client createClientFromModel(ClientModel clientModel) {
        List<String> acquaintanceList = clientAcquaintanceDao.findAcquaintancesByClientJid(clientModel.jid);
        return new Client(clientModel.id, clientModel.jid, clientModel.secret, clientModel.name, acquaintanceList);
    }
}
