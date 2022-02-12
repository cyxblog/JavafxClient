package com.cyx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    int id;
    String username;
    String password;
    String url;
    String weChatAccount;
    int loginState;
}
