package org.iatoki.judgels.sealtiel.models.domains;

import org.iatoki.judgels.commons.models.domains.AbstractJudgelsModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "sealtiel_message")
public class MessageModel extends AbstractJudgelsModel {

    public String sourceClientChannel;

    public String sourceIPAddress;

    public String targetClientChannel;

    public String messageType;

    @Column(columnDefinition = "TEXT")
    public String message;

    public int priority;
}
