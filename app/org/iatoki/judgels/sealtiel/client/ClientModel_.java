package org.iatoki.judgels.sealtiel.client;

import org.iatoki.judgels.play.model.AbstractJudgelsModel_;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ClientModel.class)
public abstract class ClientModel_ extends AbstractJudgelsModel_ {

        public static volatile SingularAttribute<ClientModel, String> adminName;
        public static volatile SingularAttribute<ClientModel, Long> totalDownload;
        public static volatile SingularAttribute<ClientModel, String> channel;
        public static volatile SingularAttribute<ClientModel, String> name;
        public static volatile SingularAttribute<ClientModel, Long> lastDownloadTime;
        public static volatile SingularAttribute<ClientModel, String> secret;
        public static volatile SingularAttribute<ClientModel, String> adminEmail;
}
