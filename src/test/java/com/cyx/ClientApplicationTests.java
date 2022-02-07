package com.cyx;

import com.cyx.pojo.ChatListItem;
import com.cyx.pojo.Friend;
import com.cyx.pojo.Message;
import com.cyx.pojo.User;
import com.cyx.service.ChatListItemServiceImpl;
import com.cyx.service.FriendServiceImpl;
import com.cyx.service.MessageServiceImpl;
import com.cyx.service.UserServiceImpl;
import com.cyx.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.swing.*;
import java.io.File;
import java.util.List;

@SpringBootTest
class ClientApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FriendServiceImpl friendService;

    @Autowired
    private ChatListItemServiceImpl chatListItemService;

    @Autowired
    private MessageServiceImpl messageService;

    @Test
    void contextLoads() {
//        File file = new File(FileUtils.getFileTypeImagePath("sss.docx"));
        Message message = messageService.getLastMessageByUserId(1);
        System.out.println(message);
    }

}
