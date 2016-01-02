package org.iatoki.judgels.sealtiel.message;

import org.iatoki.judgels.play.model.AbstractJudgelsHibernateDao;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named("messageDao")
public final class MessageHibernateDao extends AbstractJudgelsHibernateDao<MessageModel> implements MessageDao {

    public MessageHibernateDao() {
        super(MessageModel.class);
    }
}
