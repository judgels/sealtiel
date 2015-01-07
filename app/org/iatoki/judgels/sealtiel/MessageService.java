package org.iatoki.judgels.sealtiel;

public interface MessageService {
    Message createMessage(long id, String sourceClientChannel, String sourceIPAddress, String targetClientChannel, String messageType, String message, int priority);
}
