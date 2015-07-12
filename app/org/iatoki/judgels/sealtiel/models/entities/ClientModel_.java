package org.iatoki.judgels.sealtiel.models.entities;

import org.iatoki.judgels.play.models.domains.AbstractJudgelsModel_;

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
        public static volatile SingularAttribute<ClientModel, String> acquaintances;

}
