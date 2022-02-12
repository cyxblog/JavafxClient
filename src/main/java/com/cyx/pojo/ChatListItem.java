package com.cyx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatListItem {
    int id;
    int friendId;
    String url;
    String username;
    String message;
    String date;
}
