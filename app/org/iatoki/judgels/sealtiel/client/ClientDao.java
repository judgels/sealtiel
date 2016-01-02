package org.iatoki.judgels.sealtiel.client;

import com.google.inject.ImplementedBy;
import org.iatoki.judgels.play.model.JudgelsDao;

@ImplementedBy(ClientHibernateDao.class)
public interface ClientDao extends JudgelsDao<ClientModel> {

}
