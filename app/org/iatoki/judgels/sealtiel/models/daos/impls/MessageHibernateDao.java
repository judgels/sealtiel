package org.iatoki.judgels.sealtiel.models.daos.impls;

import org.iatoki.judgels.commons.models.daos.hibernate.AbstractJudgelsHibernateDao;
import org.iatoki.judgels.sealtiel.models.daos.MessageDao;
import org.iatoki.judgels.sealtiel.models.entities.MessageModel;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("messageDao")
public final class MessageHibernateDao extends AbstractJudgelsHibernateDao<MessageModel> implements MessageDao {

    public MessageHibernateDao() {
        super(MessageModel.class);
    }
}
