package org.iatoki.judgels.sealtiel.client;

import org.iatoki.judgels.play.model.AbstractJudgelsModel_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ClientModel.class)
public abstract class ClientModel_ extends AbstractJudgelsModel_ {

    public static volatile SingularAttribute<ClientModel, String> name;
    public static volatile SingularAttribute<ClientModel, String> secret;
}
