package org.iatoki.judgels.sealtiel.client.acquaintance;


import org.iatoki.judgels.play.model.AbstractModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sealtiel_client_acquaintances")
public final class ClientAcquaintanceModel extends AbstractModel {

    @Id
    @GeneratedValue
    public long id;

    public String clientJid;

    public String acquaintance;
}
