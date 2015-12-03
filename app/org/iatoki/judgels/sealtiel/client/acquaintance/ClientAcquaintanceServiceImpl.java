package org.iatoki.judgels.sealtiel.client.acquaintance;

import org.iatoki.judgels.play.IdentityUtils;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("clientAcquaintancesService")
public final class ClientAcquaintanceServiceImpl implements ClientAcquaintanceService {

    private final ClientAcquaintanceDao clientAcquaintanceDao;

    @Inject
    public ClientAcquaintanceServiceImpl(ClientAcquaintanceDao clientAcquaintanceDao) {
        this.clientAcquaintanceDao = clientAcquaintanceDao;
    }

    @Override
    public void addAcquaintance(String clientJid, String acquaintance) {
        if (clientAcquaintanceDao.existsByRelation(clientJid, acquaintance)) {
            return;
        }
        ClientAcquaintanceModel clientAcquaintanceModel = new ClientAcquaintanceModel();
        clientAcquaintanceModel.clientJid = clientJid;
        clientAcquaintanceModel.acquaintance = acquaintance;

        clientAcquaintanceDao.persist(clientAcquaintanceModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());
    }

    @Override
    public void removeAcquaintance(String clientJid, String acquaintance) {
        ClientAcquaintanceModel clientAcquaintanceModel = clientAcquaintanceDao.findByRelation(clientJid, acquaintance);
        clientAcquaintanceDao.remove(clientAcquaintanceModel);
    }
}
