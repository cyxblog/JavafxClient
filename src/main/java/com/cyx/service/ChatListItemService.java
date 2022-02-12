package com.cyx.service;

import com.cyx.pojo.ChatListItem;

import java.util.List;

public interface ChatListItemService {
    void addChatListItem(ChatListItem chatListItem);

    void deleteChatListItemById(int id);

    void updateChatListItem(ChatListItem chatListItem);

    ChatListItem getLastOneItemByUserId(int userId);

    ChatListItem getChatListItemById(int id);

    List<ChatListItem> getChatListItemsByUserId(int userId);

    List<ChatListItem> getAllChatListItems();
}
