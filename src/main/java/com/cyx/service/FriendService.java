package com.cyx.service;

import com.cyx.pojo.Friend;
import com.cyx.pojo.User;

import java.util.List;

public interface FriendService {
    void addFriend(Friend friend);

    void deleteFriendByUserId(int userId);

    void updateFriend(Friend friend);

    Friend getFriendById(int id);

    Friend getLastFriendByUserId(int userId);

    Friend getFriendByUserIdAndUsername(int userId, String username);

    List<Friend> getAllFriendsByUserId(int userId);
}
