package org.iatoki.judgels.sealtiel.models.dao.interfaces;

import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.sealtiel.models.domains.ClientModel;

import java.util.Collection;
import java.util.List;

public interface ClientDao extends JudgelsDao<ClientModel> {

    ClientModel findClientByChannel(String channel);

    List<ClientModel> findClientsByClientChannels(Collection<String> clientChannels);

}
