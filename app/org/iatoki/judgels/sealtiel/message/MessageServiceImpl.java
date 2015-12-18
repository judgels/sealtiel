package org.iatoki.judgels.sealtiel.message;

import org.iatoki.judgels.play.IdentityUtils;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;

    @Inject
    public MessageServiceImpl(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public Message createMessage(String sourceClientJid, String sourceIPAddress, String targetClientJid, String messageType, String message, int priority) {
        MessageModel messageModel = new MessageModel();
        messageModel.sourceClientJid = sourceClientJid;
        messageModel.sourceIPAddress = sourceIPAddress;
        messageModel.targetClientJid = targetClientJid;
        messageModel.messageType = messageType;
        messageModel.message = message;
        messageModel.priority = priority;

        messageDao.persist(messageModel, IdentityUtils.getUserJid(), IdentityUtils.getIpAddress());

        return createMessageFromModel(messageModel);
    }

    private Message createMessageFromModel(MessageModel messageModel) {
        return new Message(messageModel.id, messageModel.sourceClientJid, messageModel.sourceIPAddress, messageModel.targetClientJid, messageModel.messageType, messageModel.message, messageModel.timeCreate, messageModel.priority);
    }
}
