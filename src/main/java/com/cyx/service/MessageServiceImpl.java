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
    public void deleteMessageByUserId(int userId) {
        messageMapper.deleteMessageByUserId(userId);
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
    public Message getLastMessageByUserId(int userId) {
        return messageMapper.getLastMessageByUserId(userId);
    }

    @Override
    public List<Message> getMessagesByUserId(int userId) {
        return messageMapper.getMessagesByUserId(userId);
    }

    @Override
    public List<Message> getAllMessages() {
        return messageMapper.getAllMessages();
    }
}
