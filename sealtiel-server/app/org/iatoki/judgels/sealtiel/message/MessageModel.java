package org.iatoki.judgels.sealtiel.message;

import org.iatoki.judgels.play.model.AbstractJudgelsModel;
import org.iatoki.judgels.play.jid.JidPrefix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sealtiel_message")
@JidPrefix("SEMS")
public final class MessageModel extends AbstractJudgelsModel {

    public String sourceClientJid;

    public String sourceIPAddress;

    public String targetClientJid;

    public String messageType;

    @Column(columnDefinition = "LONGTEXT")
    public String message;

    public int priority;
}
