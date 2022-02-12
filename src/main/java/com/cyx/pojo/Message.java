package com.cyx.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    int id;
    int chatItemId;
    int type;
    String text;
    String fileName;
    long fileLength;
    String fileType;
    String filePath;
    String url;
}
