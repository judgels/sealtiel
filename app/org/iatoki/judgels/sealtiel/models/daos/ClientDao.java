package org.iatoki.judgels.sealtiel.models.daos;

import org.iatoki.judgels.play.models.daos.JudgelsDao;
import org.iatoki.judgels.sealtiel.models.entities.ClientModel;

import java.util.Collection;
import java.util.List;

public interface ClientDao extends JudgelsDao<ClientModel> {

    List<ClientModel> findClientsByClientJids(Collection<String> clientJids);

}
