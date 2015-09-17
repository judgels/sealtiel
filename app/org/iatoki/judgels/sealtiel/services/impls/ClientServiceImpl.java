package org.iatoki.judgels.sealtiel.services.impls;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.play.IdentityUtils;
import org.iatoki.judgels.play.JudgelsPlayUtils;
import org.iatoki.judgels.sealtiel.Client;
import org.iatoki.judgels.sealtiel.models.daos.ClientDao;
import org.iatoki.judgels.sealtiel.models.entities.ClientModel;
import org.iatoki.judgels.sealtiel.services.ClientService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
@Named("clientService")
public final class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    @Inject
    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
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
        clientModel.acquaintances = "";
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
        List<ClientModel> clientModels = clientDao.getAll();

        return clientModels.stream().map(c -> new Client(c.id, c.jid, c.secret, c.name, Arrays.asList(c.acquaintances.split(",")))).collect(Collectors.toList());
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

    @Override
    public void addClientAcquaintance(String clientJid, String acquaintanceJid) {
        ClientModel clientModel = clientDao.findByJid(clientJid);
        List<String> list = null;
        if (!"".equals(clientModel.acquaintances)) {
            list = Lists.newArrayList(clientModel.acquaintances.split(","));
        } else {
            list = new ArrayList<>();
        }
        list.add(acquaintanceJid);
        clientModel.acquaintances = StringUtils.join(list, ",");

        clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public void removeClientAcquaintance(String clientJid, String acquaintanceJid) {
        ClientModel clientModel = clientDao.findByJid(clientJid);
        if (!"".equals(clientModel.acquaintances)) {
            List<String> list = Lists.newArrayList(clientModel.acquaintances.split(","));
            list.remove(acquaintanceJid);
            clientModel.acquaintances = StringUtils.join(list, ",");

            clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
        }
    }

    private Client createClientFromModel(ClientModel clientModel) {
        return new Client(clientModel.id, clientModel.jid, clientModel.secret, clientModel.name, Arrays.asList(clientModel.acquaintances.split(",")));
    }
}
