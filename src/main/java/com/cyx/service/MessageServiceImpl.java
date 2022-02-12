package com.cyx.service;

import com.cyx.mapper.MessageMapper;
import com.cyx.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("messageService")
public class MessageServiceImpl implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void addMessage(Message message) {
        messageMapper.addMessage(message);
    }

    @Override
    public void deleteMessageById(int id) {
        messageMapper.deleteMessageById(id);
    }

    @Override
    public void deleteMessagesByChatItemId(int chatItemId) {
        messageMapper.deleteMessagesByChatItemId(chatItemId);
    }

    @Override
    public void updateMessage(Message message) {
        messageMapper.updateMessage(message);
    }

    @Override
    public Message getMessageById(int id) {
        return messageMapper.getMessageById(id);
    }

    @Override
    public Message getLastMessageByChatItemId(int chatItemId) {
        return messageMapper.getLastMessageByChatItemId(chatItemId);
    }

    @Override
    public List<Message> getMessagesByChatItemId(int chatItemId) {
        return messageMapper.getMessagesByChatItemId(chatItemId);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageMapper.getAllMessages();
    }
}
