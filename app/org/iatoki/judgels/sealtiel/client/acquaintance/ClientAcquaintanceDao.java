package org.iatoki.judgels.sealtiel.client.acquaintance;


import org.iatoki.judgels.play.model.Dao;

import java.util.List;

public interface ClientAcquaintanceDao extends Dao<Long, ClientAcquaintanceModel> {

    List<String> findAcquaintancesByClientJid(String clientJid);

    ClientAcquaintanceModel findByRelation(String clientJid, String acquaintance);

    boolean existsByRelation(String clientJid, String acquaintance);
}
