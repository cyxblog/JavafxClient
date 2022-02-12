package com.cyx.mapper;


import com.cyx.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper {

    void addUser(User user);

    void deleteUserById(int id);

    void updateUser(User user);

    void updateUserByUsername(User user);

    User getUserById(int id);

    User getUserByLoginState(int loginState);

    User getUserByUsername(String username);

    List<User> getAllUsers();


}
