package com.cyx.service;

import com.cyx.pojo.ChatListItem;

import java.util.List;

public interface ChatListItemService {
    void addChatListItem(ChatListItem chatListItem);

    void deleteChatListItemById(int id);

    void updateChatListItem(ChatListItem chatListItem);

    ChatListItem getLastOneItem();

    ChatListItem getChatListItemById(int id);

    List<ChatListItem> getAllChatListItems();
}
