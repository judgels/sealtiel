package org.iatoki.judgels.sealtiel.models.entities;

import org.iatoki.judgels.play.models.JidPrefix;
import org.iatoki.judgels.play.models.domains.AbstractJudgelsModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sealtiel_client")
@JidPrefix("SECL")
public final class ClientModel extends AbstractJudgelsModel {

    public String secret;

    public String name;

    public String adminName;

    public String adminEmail;

    @Column(columnDefinition = "TEXT")
    public String acquaintances;

    public long totalDownload;

    public long lastDownloadTime;
}
