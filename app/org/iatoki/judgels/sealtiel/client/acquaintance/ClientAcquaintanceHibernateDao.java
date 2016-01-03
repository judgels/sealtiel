package org.iatoki.judgels.sealtiel.client.acquaintance;

import org.iatoki.judgels.play.model.AbstractHibernateDao;
import play.db.jpa.JPA;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Singleton
@Named("clientAcquaintancesDao")
public final class ClientAcquaintanceHibernateDao extends AbstractHibernateDao<Long, ClientAcquaintanceModel> implements ClientAcquaintanceDao {

    public ClientAcquaintanceHibernateDao() {
        super(ClientAcquaintanceModel.class);
    }

    @Override
    public List<String> findAcquaintancesByClientJid(String clientJid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<ClientAcquaintanceModel> root = query.from(ClientAcquaintanceModel.class);

        query.select(root.get(ClientAcquaintanceModel_.acquaintance)).where(cb.equal(root.get(ClientAcquaintanceModel_.clientJid), clientJid));
        return JPA.em().createQuery(query).getResultList();
    }

    @Override
    public ClientAcquaintanceModel findByRelation(String clientJid, String acquaintanceJid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<ClientAcquaintanceModel> query = cb.createQuery(ClientAcquaintanceModel.class);

        Root<ClientAcquaintanceModel> root = query.from(ClientAcquaintanceModel.class);

        query.where(
                cb.and(
                        cb.equal(root.get(ClientAcquaintanceModel_.clientJid), clientJid),
                        cb.equal(root.get(ClientAcquaintanceModel_.acquaintance), acquaintanceJid))
        );

        return JPA.em().createQuery(query).getSingleResult();
    }

    @Override
    public boolean existsByRelation(String clientJid, String acquaintanceJid) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);

        Root<ClientAcquaintanceModel> root = query.from(ClientAcquaintanceModel.class);

        query.select(cb.count(root)).where(
                cb.and(
                        cb.equal(root.get(ClientAcquaintanceModel_.clientJid), clientJid),
                        cb.equal(root.get(ClientAcquaintanceModel_.acquaintance), acquaintanceJid))
        );

        return (JPA.em().createQuery(query).getSingleResult() != 0);
    }
}
