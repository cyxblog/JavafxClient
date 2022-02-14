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

    void deleteFriendByUserId(int userId);

    void updateFriend(Friend friend);

    Friend getFriendById(int id);

    Friend getLastFriendByUserId(int userId);

    Friend getFriendByUserIdAndUsername(int userId, String username);

    List<Friend> getAllFriendsByUserId(int userId);
}
