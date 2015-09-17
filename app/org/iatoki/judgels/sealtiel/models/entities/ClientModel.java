package org.iatoki.judgels.sealtiel.models.entities;

import org.iatoki.judgels.play.models.JidPrefix;
import org.iatoki.judgels.play.models.entities.AbstractJudgelsModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sealtiel_client")
@JidPrefix("SECL")
public final class ClientModel extends AbstractJudgelsModel {

    public String name;

    public String secret;

    @Column(columnDefinition = "TEXT")
    public String acquaintances;
}
