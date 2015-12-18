package org.iatoki.judgels.sealtiel.client;

import com.google.common.collect.ImmutableList;
import org.iatoki.judgels.play.models.daos.impls.AbstractJudgelsHibernateDao;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

@Singleton
@Named("clientDao")
public final class ClientHibernateDao extends AbstractJudgelsHibernateDao<ClientModel> implements ClientDao {

    public ClientHibernateDao() {
        super(ClientModel.class);
    }

    @Override
    protected List<SingularAttribute<ClientModel, String>> getColumnsFilterableByString() {
        return ImmutableList.of(ClientModel_.name, ClientModel_.adminName, ClientModel_.adminEmail);
    }
}
