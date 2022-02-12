package com.cyx.service;


import com.cyx.pojo.User;

import java.util.List;


public interface UserService {

    void addUser(User user);

    void deleteUserById(int id);

    void updateUser(User user);

    void updateUserByUsername(User user);

    User getUserById(int id);

    User getUserByLoginState(int loginState);

    User getUserByUsername(String username);

    List<User> getAllUsers();
}
