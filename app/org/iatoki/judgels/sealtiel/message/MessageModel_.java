package org.iatoki.judgels.sealtiel.message;

import org.iatoki.judgels.play.models.entities.AbstractJudgelsModel_;

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
