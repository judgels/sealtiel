package org.iatoki.judgels.sealtiel.models.daos.impls;

import org.iatoki.judgels.commons.models.daos.hibernate.AbstractJudgelsHibernateDao;
import org.iatoki.judgels.sealtiel.models.daos.MessageDao;
import org.iatoki.judgels.sealtiel.models.dbs.MessageModel;

public final class MessageHibernateDao extends AbstractJudgelsHibernateDao<MessageModel> implements MessageDao {

    public MessageHibernateDao() {
        super(MessageModel.class);
    }

}
