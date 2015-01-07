package org.iatoki.judgels.sealtiel.models.domains;

import org.iatoki.judgels.commons.models.domains.AbstractJudgelsModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sealtiel_client")
public class ClientModel extends AbstractJudgelsModel {

    public String secret;

    public String channel;

    public String name;

    public String adminName;

    public String adminEmail;

    @Column(columnDefinition = "TEXT")
    public String acquaintances;

    public long totalDownload;

    public long lastDownloadTime;
}
