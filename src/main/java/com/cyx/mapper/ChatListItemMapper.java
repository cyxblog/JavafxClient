package com.cyx.mapper;

import com.cyx.pojo.ChatListItem;
import com.cyx.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ChatListItemMapper {
    void addChatListItem(ChatListItem chatListItem);

    void deleteChatListItemById(int id);

    void updateChatListItem(ChatListItem chatListItem);

    ChatListItem getLastOneItem();

    ChatListItem getChatListItemById(int id);

    List<ChatListItem> getAllChatListItems();
}
