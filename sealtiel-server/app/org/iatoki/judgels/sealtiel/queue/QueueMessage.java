package org.iatoki.judgels.sealtiel.queue;

public final class QueueMessage {

    private final long tag;
    private final String content;

    public QueueMessage(long tag, String content) {
        this.tag = tag;
        this.content = content;
    }

    public long getTag() {
        return tag;
    }

    public String getContent() {
        return content;
    }
}
