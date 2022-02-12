package com.cyx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Friend {

    int id;
    int userId;
    String username;
    String url;
    int sex;
    String sign;
    String remark;
    String region;
    String weChatAccount;
    String origin;
    String publicKeyPath;
}
