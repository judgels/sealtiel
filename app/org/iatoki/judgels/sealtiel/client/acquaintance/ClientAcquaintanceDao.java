package org.iatoki.judgels.sealtiel.client.acquaintance;

import com.google.inject.ImplementedBy;
import org.iatoki.judgels.play.model.Dao;

import java.util.List;

@ImplementedBy(ClientAcquaintanceHibernateDao.class)
public interface ClientAcquaintanceDao extends Dao<Long, ClientAcquaintanceModel> {

    List<String> findAcquaintancesByClientJid(String clientJid);

    ClientAcquaintanceModel findByRelation(String clientJid, String acquaintance);

    boolean existsByRelation(String clientJid, String acquaintance);
}
