package com.cyx.mapper;

import com.cyx.pojo.Friend;
import com.cyx.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FriendMapper {
    void addFriend(Friend friend);

    void deleteFriendById(int id);

    void updateFriend(Friend friend);

    Friend getFriendById(int id);

    List<Friend> getAllFriends();
}
