package org.iatoki.judgels.sealtiel.models.dbs.metas;

import org.iatoki.judgels.commons.models.domains.AbstractJudgelsModel_;
import org.iatoki.judgels.sealtiel.models.dbs.MessageModel;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MessageModel.class)
public abstract class MessageModel_ extends AbstractJudgelsModel_ {

        public static volatile SingularAttribute<MessageModel, String> targetClientJid;
        public static volatile SingularAttribute<MessageModel, String> messageType;
        public static volatile SingularAttribute<MessageModel, String> sourceIPAddress;
        public static volatile SingularAttribute<MessageModel, String> sourceClientJid;
        public static volatile SingularAttribute<MessageModel, String> message;
        public static volatile SingularAttribute<MessageModel, Integer> priority;

}
