package org.iatoki.judgels.sealtiel;

import org.iatoki.judgels.commons.IdentityUtils;
import org.iatoki.judgels.sealtiel.models.dao.interfaces.MessageDao;
import org.iatoki.judgels.sealtiel.models.domains.MessageModel;

public class MessageServiceImpl implements MessageService {

    private MessageDao messageDao;

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
