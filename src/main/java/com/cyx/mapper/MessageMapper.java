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

    void deleteMessageByUserId(int userId);

    void updateMessage(Message message);

    Message getMessageById(int id);

    Message getLastMessageByUserId(int userId);

    List<Message> getMessagesByUserId(int userId);

    List<Message> getAllMessages();
}
