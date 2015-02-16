package org.iatoki.judgels.sealtiel;

public class Message {

    private long id;
    private String sourceClientJid;
    private String sourceIPAddress;
    private String targetClientJid;
    private String messageType;
    private String message;
    private long timestamp;
    private int priority;

    public Message(long id, String sourceClientJid, String sourceIPAddress, String targetClientJid, String messageType, String message, long timestamp, int priority) {
        this.id = id;
        this.sourceClientJid = sourceClientJid;
        this.sourceIPAddress = sourceIPAddress;
        this.targetClientJid = targetClientJid;
        this.messageType = messageType;
        this.message = message;
        this.timestamp = timestamp;
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public String getSourceIPAddress() {
        return sourceIPAddress;
    }

    public String getSourceClientJid() {
        return sourceClientJid;
    }

    public String getTargetClientJid() {
        return targetClientJid;
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
