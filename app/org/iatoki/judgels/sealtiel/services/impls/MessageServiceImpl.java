package org.iatoki.judgels.sealtiel.services.impls;

import org.iatoki.judgels.commons.IdentityUtils;
import org.iatoki.judgels.sealtiel.Message;
import org.iatoki.judgels.sealtiel.models.daos.MessageDao;
import org.iatoki.judgels.sealtiel.models.entities.MessageModel;
import org.iatoki.judgels.sealtiel.services.MessageService;

public final class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;

    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public Message createMessage(long id, String sourceClientJid, String sourceIPAddress, String targetClientJid, String messageType, String message, int priority) {
        MessageModel messageModel = new MessageModel();
        messageModel.sourceClientJid = sourceClientJid;
        messageModel.sourceIPAddress = sourceIPAddress;
        messageModel.targetClientJid = targetClientJid;
        messageModel.messageType = messageType;
        messageModel.message = message;
        messageModel.priority = priority;

        messageDao.persist(messageModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());

        return new Message(messageModel.id, messageModel.sourceClientJid, messageModel.sourceIPAddress, messageModel.targetClientJid, messageModel.messageType, messageModel.message, messageModel.timeCreate, messageModel.priority);
    }
}