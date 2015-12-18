package org.iatoki.judgels.sealtiel.client;

import com.google.inject.ImplementedBy;
import org.iatoki.judgels.play.models.daos.JudgelsDao;

@ImplementedBy(ClientHibernateDao.class)
public interface ClientDao extends JudgelsDao<ClientModel> {

}
