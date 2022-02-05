package com.cyx.service;

import com.cyx.mapper.ChatListItemMapper;
import com.cyx.pojo.ChatListItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("chatListItemService")
public class ChatListItemServiceImpl implements ChatListItemService{
    @Autowired
    private ChatListItemMapper chatListItemMapper;

    @Override
    public void addChatListItem(ChatListItem chatListItem) {
        chatListItemMapper.addChatListItem(chatListItem);
    }

    @Override
    public void deleteChatListItemById(int id) {
        chatListItemMapper.deleteChatListItemById(id);
    }

    @Override
    public void updateChatListItem(ChatListItem chatListItem) {
        chatListItemMapper.updateChatListItem(chatListItem);
    }

    @Override
    public ChatListItem getLastOneItem() {
        return chatListItemMapper.getLastOneItem();
    }

    @Override
    public ChatListItem getChatListItemById(int id) {
        return chatListItemMapper.getChatListItemById(id);
    }

    @Override
    public List<ChatListItem> getAllChatListItems() {
        return chatListItemMapper.getAllChatListItems();
    }
}
