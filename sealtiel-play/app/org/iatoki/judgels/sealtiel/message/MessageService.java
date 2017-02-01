package org.iatoki.judgels.sealtiel.message;

import com.google.inject.ImplementedBy;

@ImplementedBy(MessageServiceImpl.class)
public interface MessageService {
    Message createMessage(String sourceClientJid, String sourceIPAddress, String targetClientJid, String messageType, String message, int priority);
}
