package org.iatoki.judgels.sealtiel.models.daos.impls;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.commons.models.daos.hibernate.AbstractJudgelsHibernateDao;
import org.iatoki.judgels.sealtiel.models.daos.ClientDao;
import org.iatoki.judgels.sealtiel.models.dbs.ClientModel;
import org.iatoki.judgels.sealtiel.models.dbs.metas.ClientModel_;
import play.db.jpa.JPA;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Collection;
import java.util.List;

public final class ClientHibernateDao extends AbstractJudgelsHibernateDao<ClientModel> implements ClientDao {

    public ClientHibernateDao() {
        super(ClientModel.class);
    }

    @Override
    public List<ClientModel> findClientsByClientJids(Collection<String> clientJids) {
        CriteriaBuilder cb = JPA.em().getCriteriaBuilder();
        CriteriaQuery<ClientModel> query = cb.createQuery(ClientModel.class);
        Root<ClientModel> client = query.from(ClientModel.class);
        query.where(client.get(ClientModel_.jid).in(clientJids));

        return JPA.em().createQuery(query).getResultList();
    }

    @Override
    protected List<SingularAttribute<ClientModel, String>> getColumnsFilterableByString() {
        return ImmutableList.of(ClientModel_.name, ClientModel_.adminName, ClientModel_.adminEmail);
    }
}
