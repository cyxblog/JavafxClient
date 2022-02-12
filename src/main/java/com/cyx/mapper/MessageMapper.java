package com.cyx.mapper;

import com.cyx.pojo.Friend;
import com.cyx.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MessageMapper {
    void addMessage(Message message);

    void deleteMessageById(int id);

    void deleteMessagesByChatItemId(int chatItemId);

    void updateMessage(Message message);

    Message getMessageById(int id);

    Message getLastMessageByChatItemId(int chatItemId);

    List<Message> getMessagesByChatItemId(int chatItemId);

    List<Message> getAllMessages();
}
