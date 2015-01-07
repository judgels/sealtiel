package org.iatoki.judgels.sealtiel.models.dao.hibernate;

import org.iatoki.judgels.commons.models.daos.hibernate.AbstractJudgelsHibernateDao;
import org.iatoki.judgels.sealtiel.models.dao.interfaces.ClientDao;
import org.iatoki.judgels.sealtiel.models.domains.ClientModel;
import org.iatoki.judgels.sealtiel.models.domains.ClientModel_;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;

public class ClientHibernateDao extends AbstractJudgelsHibernateDao<ClientModel> implements ClientDao {

    @Override
    public ClientModel findClientByChannel(String channel) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<ClientModel> query = cb.createQuery(ClientModel.class);
        Root<ClientModel> client = query.from(ClientModel.class);
        query.where(cb.equal(client.get(ClientModel_.channel), channel));

        return JPA.em().createQuery(query).getSingleResult();
    }

    @Override
    public List<ClientModel> findClientsByClientChannels(Collection<String> clientChannels) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<ClientModel> query = cb.createQuery(ClientModel.class);
        Root<ClientModel> client = query.from(ClientModel.class);
        query.where(client.get(ClientModel_.channel).in(clientChannels));

        return JPA.em().createQuery(query).getResultList();
    }
}
