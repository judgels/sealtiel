package org.iatoki.judgels.sealtiel.message;

import com.google.inject.ImplementedBy;
import org.iatoki.judgels.play.model.JudgelsDao;

@ImplementedBy(MessageHibernateDao.class)
public interface MessageDao extends JudgelsDao<MessageModel> {

}
