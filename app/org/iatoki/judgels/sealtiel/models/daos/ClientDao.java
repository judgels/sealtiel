package org.iatoki.judgels.sealtiel.models.daos;

import org.iatoki.judgels.commons.models.daos.interfaces.JudgelsDao;
import org.iatoki.judgels.sealtiel.models.dbs.ClientModel;

import java.util.Collection;
import java.util.List;

public interface ClientDao extends JudgelsDao<ClientModel> {

    List<ClientModel> findClientsByClientJids(Collection<String> clientJids);

}
