package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.sealtiel.models.domains.MessageModel;

public class Message {

    private long id;
    private String sourceClientChannel;
    private String sourceIPAddress;
    private String targetClientChannel;
    private String messageType;
    private String message;
    private long timestamp;
    private int priority;

    public Message(MessageModel messageModel) {
        this.id = messageModel.id;
        this.sourceClientChannel = messageModel.sourceClientChannel;
        this.sourceIPAddress = messageModel.sourceIPAddress;
        this.targetClientChannel = messageModel.targetClientChannel;
        this.messageType = messageModel.messageType;
        this.message = messageModel.message;
        this.timestamp = messageModel.timeCreate;
        this.priority = messageModel.priority;
    }

    public long getId() {
        return id;
    }

    public String getSourceClientChannel() {
        return sourceClientChannel;
    }

    public String getSourceIPAddress() {
        return sourceIPAddress;
    }

    public String getTargetClientChannel() {
        return targetClientChannel;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getPriority() {
        return priority;
    }
}
