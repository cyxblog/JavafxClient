package com.cyx.service;

import com.cyx.mapper.FriendMapper;
import com.cyx.pojo.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("friendService")
public class FriendServiceImpl implements FriendService{
    @Autowired
    private FriendMapper friendMapper;

    @Override
    public void addFriend(Friend friend) {
        friendMapper.addFriend(friend);
    }

    @Override
    public void deleteFriendByUserId(int userId) {
        friendMapper.deleteFriendByUserId(userId);
    }

    @Override
    public void updateFriend(Friend friend) {
        friendMapper.updateFriend(friend);
    }

    @Override
    public Friend getFriendById(int id) {
        return friendMapper.getFriendById(id);
    }

    @Override
    public List<Friend> getAllFriendsByUserId(int userId) {
        return friendMapper.getAllFriendsByUserId(userId);
    }
}
