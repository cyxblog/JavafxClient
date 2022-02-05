package com.cyx.controller;


import com.cyx.ClientApplication;
import com.cyx.component.ChatListItemPane;
import com.cyx.component.MessageItemPane;
import com.cyx.constant.MessageType;
import com.cyx.pojo.ChatListItem;
import com.cyx.pojo.Message;
import com.cyx.pojo.User;
import com.cyx.service.ChatListItemServiceImpl;
import com.cyx.service.MessageServiceImpl;
import com.cyx.service.UserServiceImpl;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController
public class MainController implements Initializable {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ChatListItemServiceImpl chatListItemService;

    @Autowired
    private MessageServiceImpl messageService;

    private Stage mainStage;

    @FXML
    private SplitPane messageSplitPane;

    @FXML
    private VBox chatVBox;

    @FXML
    private VBox messageVBox;

    @FXML
    private Label topNameLabel;

    @FXML
    private Button chatInfoButton;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private ImageView weChatLogo;

    @FXML
    private ScrollPane messageScrollPane;

    int currentItemId = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        mainStage = ClientApplication.getStage();
    }

    /**
     * 初始化数据渲染
     */
    public void initView() {

        messageSplitPane.setVisible(false);
        topNameLabel.setVisible(false);
        chatInfoButton.setVisible(false);
        weChatLogo.setVisible(true);

        messageVBox.prefWidthProperty().bind(messageSplitPane.widthProperty());

        List<ChatListItem> allChatListItems = chatListItemService.getAllChatListItems();
        List<ChatListItemPane> itemPanes = new ArrayList<>();
        for (ChatListItem item : allChatListItems) {
            ChatListItemPane itemPane = getChatListItemPane(item);
            itemPanes.add(itemPane);
        }
        chatVBox.getChildren().addAll(itemPanes);

    }

    /**
     * 根据聊天列表项数据渲染界面
     * @param item 聊天列表项数据
     * @return 聊天列表项
     */
    private ChatListItemPane getChatListItemPane(ChatListItem item) {
        ChatListItemPane itemPane = new ChatListItemPane(item);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem topItem = new MenuItem("置顶");
        MenuItem readItem = new MenuItem("标为未读");
        MenuItem botherItem = new MenuItem("消息免打扰");
        MenuItem windowItem = new MenuItem("在独立窗口中打开");
        MenuItem visibleItem = new MenuItem("不显示聊天");
        MenuItem deleteItem = new MenuItem("删除聊天");
        contextMenu.getItems().addAll(topItem, readItem, botherItem, windowItem,
                visibleItem, deleteItem);

        deleteItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                itemPane.setVisible(false);
                itemPane.setManaged(false);

                if(currentItemId == item.getId()){
                    messageSplitPane.setVisible(false);
                    topNameLabel.setVisible(false);
                    chatInfoButton.setVisible(false);
                    weChatLogo.setVisible(true);
                }

                chatListItemService.deleteChatListItemById(item.getId());
                messageService.deleteMessageByUserId(item.getId());
            }
        });

        itemPane.setOnContextMenuRequested(event ->
                contextMenu.show(itemPane, event.getScreenX(), event.getScreenY()));

        itemPane.setOnMouseClicked(event -> {
            if(currentItemId == item.getId()){
                return;
            }

            weChatLogo.setVisible(false);
            topNameLabel.setVisible(true);
            chatInfoButton.setVisible(true);
            messageSplitPane.setVisible(true);

            // 清除上一个好友聊天信息并显示当前好友聊天信息
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    messageVBox.getChildren().removeAll(messageVBox.getChildren());
                    List<Message> messages = messageService.getMessagesByUserId(item.getId());
                    List<MessageItemPane> messageItemPanes = new ArrayList<>();
                    for (Message message : messages) {
                        MessageItemPane messageItemPane = new MessageItemPane(message);
                        messageItemPanes.add(messageItemPane);
                    }
                    System.out.println(messages);
                    messageVBox.getChildren().addAll(messageItemPanes);
                    topNameLabel.setText(item.getUsername());

                    currentItemId = item.getId();

                }
            });

        });
        return itemPane;
    }

    /**
     * 发送按钮点击事件
     */
    public void onSendMessage(){
        String text = inputTextArea.getText().trim();
        if(text.equals("")){
            return;
        }
        User user = userService.getUserById(1);
        String url = user.getUrl();
        System.out.println("currentItemId==>"+currentItemId);
        Message message = new Message(-1, currentItemId, MessageType.TEXT_MESSAGE_SEND,
                text,null,0,null,null,url);
        messageService.addMessage(message);
        MessageItemPane messageItemPane = new MessageItemPane(message);
        messageVBox.getChildren().add(messageItemPane);
        inputTextArea.clear();
    }

    /**
     * 加号按钮点击事件
     */
    public void onAddButtonClicked(){
        ChatListItem lastOneItem = chatListItemService.getLastOneItem();
        int id = lastOneItem.getId();
        ChatListItem item = new ChatListItem(id + 1, "/images/wang1.jpeg", "张宇锖", "", "22/2/25");
        chatListItemService.addChatListItem(item);
        ChatListItemPane chatListItemPane = getChatListItemPane(item);
        chatVBox.getChildren().add(chatListItemPane);
    }

    /**
     * 选择文件
     */
    public void onChooseFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开");
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有文件", "*.*"),
                new FileChooser.ExtensionFilter("图片", "*.png;*.jpg;*.jpeg;*.gif"));
        List<File> files = fileChooser.showOpenMultipleDialog(mainStage);
        if(files != null){
            for (File file : files) {
                String fileName = file.getName();
                long fileLength = file.length();
                String filePath = file.getAbsolutePath();
                String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];
                User user = userService.getUserById(1);
                String url = user.getUrl();
                System.out.println("currentItemId==>"+currentItemId);
                Message message = new Message(-1, currentItemId, MessageType.FILE_MESSAGE_SEND,
                        null,fileName,fileLength,fileType,filePath,url);
                messageService.addMessage(message);
                MessageItemPane messageItemPane = new MessageItemPane(message);
                messageVBox.getChildren().add(messageItemPane);
            }
        }
    }

    /**
     * 窗口置顶
     */
    public void onWindowTop() {
        mainStage.setAlwaysOnTop(true);
    }

    /**
     * 窗口隐藏
     */
    public void onWindowHide() {
        mainStage.setIconified(true);
    }

    /**
     * 窗口全屏
     */
    public void onWindowFull() {
        mainStage.setFullScreen(true);
    }

    /**
     * 窗口关闭
     */
    public void onWindowClosed() {
        mainStage.close();
    }
}
