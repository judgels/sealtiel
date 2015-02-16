package org.iatoki.judgels.sealtiel.models.dao.hibernate;

import org.iatoki.judgels.commons.models.daos.hibernate.AbstractJudgelsHibernateDao;
import org.iatoki.judgels.sealtiel.models.dao.interfaces.MessageDao;
import org.iatoki.judgels.sealtiel.models.domains.MessageModel;

public class MessageHibernateDao extends AbstractJudgelsHibernateDao<MessageModel> implements MessageDao {

    public MessageHibernateDao() {
        super(MessageModel.class);
    }

}
