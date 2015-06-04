package org.iatoki.judgels.sealtiel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.iatoki.judgels.commons.IdentityUtils;
import org.iatoki.judgels.commons.JudgelsUtils;
import org.iatoki.judgels.sealtiel.models.dao.interfaces.ClientDao;
import org.iatoki.judgels.sealtiel.models.domains.ClientModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public Client findClientByClientId(long clientId) {
        ClientModel clientModel = clientDao.findById(clientId);
        return new Client(clientModel.id, clientModel.jid, clientModel.secret, clientModel.name, clientModel.adminName, clientModel.adminEmail, Arrays.asList(clientModel.acquaintances.split(",")), clientModel.totalDownload, clientModel.lastDownloadTime);
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
        clientModel.secret = JudgelsUtils.hashMD5(UUID.randomUUID().toString());

        clientDao.persist(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public List<Client> findAllClient() {
        List<ClientModel> clientModels = clientDao.findAll();

        return clientModels.stream().map(c -> new Client(c.id, c.jid, c.secret, c.name, c.adminName, c.adminEmail, Arrays.asList(c.acquaintances.split(",")), c.totalDownload, c.lastDownloadTime)).collect(Collectors.toList());
    }

    @Override
    public boolean existByClientJid(String clientJid) {
        return clientDao.existsByJid(clientJid);
    }

    @Override
    public Client findClientByClientJid(String clientJid) {
        ClientModel clientModel = clientDao.findByJid(clientJid);
        return new Client(clientModel.id, clientModel.jid, clientModel.secret, clientModel.name, clientModel.adminName, clientModel.adminEmail, Arrays.asList(clientModel.acquaintances.split(",")), clientModel.totalDownload, clientModel.lastDownloadTime);
    }

    @Override
    public List<Client> findClientsByClientJids(List<String> clientJids) {
        ImmutableList.Builder<Client> result = ImmutableList.builder();
        List<ClientModel> clientModels = clientDao.findClientsByClientJids(clientJids);

        for (ClientModel clientModel : clientModels) {
            result.add(new Client(clientModel.id, clientModel.jid, clientModel.secret, clientModel.name, clientModel.adminName, clientModel.adminEmail, Arrays.asList(clientModel.acquaintances.split(",")), clientModel.totalDownload, clientModel.lastDownloadTime));
        }

        return result.build();
    }

    @Override
    public void downloadLib(String clientJid) {
        ClientModel clientModel = clientDao.findByJid(clientJid);
        clientModel.totalDownload++;
        clientModel.lastDownloadTime = System.currentTimeMillis();

        clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public void addAcquaintance(String clientJid, String acquaintance) {
        ClientModel clientModel = clientDao.findByJid(clientJid);
        List<String> list = null;
        if (!"".equals(clientModel.acquaintances)) {
            list = Lists.newArrayList(clientModel.acquaintances.split(","));
        } else {
            list = new ArrayList<>();
        }
        list.add(acquaintance);
        clientModel.acquaintances = StringUtils.join(list, ",");

        clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public void removeAcquaintance(String clientJid, String acquaintance) {
        ClientModel clientModel = clientDao.findByJid(clientJid);
        if (!"".equals(clientModel.acquaintances)) {
            List<String> list = Lists.newArrayList(clientModel.acquaintances.split(","));
            list.remove(acquaintance);
            clientModel.acquaintances = StringUtils.join(list, ",");

            clientDao.edit(clientModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
        }
    }
}
