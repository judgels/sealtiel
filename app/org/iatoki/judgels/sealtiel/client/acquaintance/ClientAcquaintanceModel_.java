package org.iatoki.judgels.sealtiel.client.acquaintance;

import org.iatoki.judgels.play.model.AbstractModel_;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(ClientAcquaintanceModel.class)
public abstract class ClientAcquaintanceModel_ extends AbstractModel_ {

    public static volatile SingularAttribute<ClientAcquaintanceModel, Long> id;
    public static volatile SingularAttribute<ClientAcquaintanceModel, String> clientJid;
    public static volatile SingularAttribute<ClientAcquaintanceModel, String> acquaintanceJid;
}
