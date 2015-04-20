package org.iatoki.judgels.sealtiel.models.domains;

import org.iatoki.judgels.commons.models.JidPrefix;
import org.iatoki.judgels.commons.models.domains.AbstractJudgelsModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sealtiel_message")
@JidPrefix("SEMS")
public class MessageModel extends AbstractJudgelsModel {

    public String sourceClientJid;

    public String sourceIPAddress;

    public String targetClientJid;

    public String messageType;

    @Column(columnDefinition = "LONGTEXT")
    public String message;

    public int priority;
}
