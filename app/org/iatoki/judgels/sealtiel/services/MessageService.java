package org.iatoki.judgels.sealtiel.services;

import org.iatoki.judgels.sealtiel.Message;

public interface MessageService {
    Message createMessage(String sourceClientJid, String sourceIPAddress, String targetClientJid, String messageType, String message, int priority);
}
