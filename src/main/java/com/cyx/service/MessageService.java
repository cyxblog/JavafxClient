package com.cyx.service;

import com.cyx.pojo.Message;

import java.util.List;

public interface MessageService {

    void addMessage(Message message);

    void deleteMessageById(int id);

    void deleteMessageByUserId(int userId);

    void updateMessage(Message message);

    Message getMessageById(int id);

    List<Message> getMessagesByUserId(int userId);

    List<Message> getAllMessages();
}
