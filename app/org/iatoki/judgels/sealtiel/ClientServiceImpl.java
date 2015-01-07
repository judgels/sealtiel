package org.iatoki.judgels.sealtiel;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.IdentityUtils;
import org.iatoki.judgels.sealtiel.models.dao.interfaces.ClientDao;
import org.iatoki.judgels.sealtiel.models.domains.ClientModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientServiceImpl implements ClientService {

    private ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public Client findClientById(long clientId) {
        return new Client(clientDao.findById(clientId));
    }

    @Override
    public void createClient(String name, String adminName, String adminEmail) {
        ClientModel clientModel = new ClientModel();
        clientModel.name = name;
        clientModel.adminName = adminName;
        clientModel.adminEmail = adminEmail;
        clientModel.acquaintances = "";
        clientModel.totalDownload = 0;
        clientModel.lastDownloadTime = System.currentTimeMillis();
        clientModel.channel = UUID.randomUUID().toString();
        clientModel.secret = UUID.randomUUID().toString();

        // TODO generate clientChannel and clientSecret
        clientDao.persist(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public List<Client> findAllClient() {
        List<ClientModel> clientModels = clientDao.findAll();

        return clientModels.stream().map(c -> new Client(c)).collect(Collectors.toList());
    }

    @Override
    public Client findClientByClientId(String clientId) {
        return new Client(clientDao.findByJid(clientId));
    }

    @Override
    public Client findClientByChannel(String channel) {
        return new Client(clientDao.findClientByChannel(channel));
    }

    @Override
    public List<Client> findClientsByClientChannels(List<String> clientChannels) {
        List<Client> result = new ArrayList<>();
        List<ClientModel> clientModels = clientDao.findClientsByClientChannels(clientChannels);

        for (ClientModel clientModel : clientModels) {
            result.add(new Client(clientModel));
        }

        return result;
    }

    @Override
    public void downloadLib(String clientId) {
        ClientModel clientModel = clientDao.findByJid(clientId);
        clientModel.totalDownload++;
        clientModel.lastDownloadTime = System.currentTimeMillis();

        clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public void addAcquaintance(String clientId, String acquaintance) {
        ClientModel clientModel = clientDao.findByJid(clientId);
        List<String> list = null;
        if (!"".equals(clientModel.acquaintances)) {
            list = Arrays.asList(clientModel.acquaintances.split(","));
        } else {
            list = new ArrayList<>();
        }
        list.add(acquaintance);
        clientModel.acquaintances = StringUtils.join(list, ",");

        clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public void removeAcquaintance(String clientId, String acquaintance) {
        ClientModel clientModel = clientDao.findByJid(clientId);
        if (!"".equals(clientModel.acquaintances)) {
            List<String> list = Lists.newArrayList(clientModel.acquaintances.split(","));
            list.remove(acquaintance);
            clientModel.acquaintances = StringUtils.join(list, ",");

            clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
        }
    }
}
