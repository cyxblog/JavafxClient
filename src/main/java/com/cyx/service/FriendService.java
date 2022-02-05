package com.cyx.service;

import com.cyx.pojo.Friend;

import java.util.List;

public interface FriendService {
    void addFriend(Friend friend);

    void deleteFriendById(int id);

    void updateFriend(Friend friend);

    Friend getFriendById(int id);

    List<Friend> getAllFriends();
}
