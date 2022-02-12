package com.cyx.service;

import com.cyx.pojo.Message;

import java.util.List;

public interface MessageService {

    void addMessage(Message message);

    void deleteMessageById(int id);

    void deleteMessagesByChatItemId(int chatItemId);

    void updateMessage(Message message);

    Message getMessageById(int id);

    Message getLastMessageByChatItemId(int chatItemId);

    List<Message> getMessagesByChatItemId(int chatItemId);

    List<Message> getAllMessages();
}
