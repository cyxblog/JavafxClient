package com.cyx.service;


import com.cyx.pojo.User;

import java.util.List;


public interface UserService {

    void addUser(User user);

    void deleteUserById(int id);

    void updateUser(User user);

    User getUserById(int id);

    List<User> getAllUsers();
}
