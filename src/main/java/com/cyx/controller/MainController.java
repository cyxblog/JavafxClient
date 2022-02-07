package com.cyx.controller;


import com.cyx.ClientApplication;
import com.cyx.component.ChatListItemPane;
import com.cyx.component.FileDialogPane;
import com.cyx.component.MessageItemPane;
import com.cyx.constant.MessageType;
import com.cyx.handler.DragWindowHandler;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
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
    private AnchorPane mainView;

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
    private Button topButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private ImageView weChatLogo;

    @FXML
    private ScrollPane messageScrollPane;

    private String profileUrl;

    int currentItemId = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initView();
    }

    /**
     * 初始化数据渲染
     */
    public void initView() {

        mainStage = ClientApplication.getStage();

        DragWindowHandler handler = new DragWindowHandler(mainStage);
        mainView.setOnMousePressed(handler);
        mainView.setOnMouseDragged(handler);

        messageSplitPane.setVisible(false);
        messageSplitPane.setManaged(false);
        topNameLabel.setVisible(false);
        chatInfoButton.setVisible(false);

        weChatLogo.setVisible(true);
        messageVBox.prefWidthProperty().bind(messageSplitPane.widthProperty());

        List<ChatListItem> allChatListItems = chatListItemService.getAllChatListItems();
        List<ChatListItemPane> itemPanes = new ArrayList<>();
        for (ChatListItem item : allChatListItems) {
            Message lastMessage = messageService.getLastMessageByUserId(item.getId());
            if (lastMessage == null) {
                item.setMessage("");
            } else {
                if (lastMessage.getType() == MessageType.TEXT_MESSAGE_SEND ||
                        lastMessage.getType() == MessageType.TEXT_MESSAGE_RECEIVE) {
                    item.setMessage(lastMessage.getText());
                } else if (lastMessage.getType() == MessageType.IMAGE_MESSAGE_SEND ||
                        lastMessage.getType() == MessageType.IMAGE_MESSAGE_RECEIVE) {
                    item.setMessage("[图片]");
                } else {
                    item.setMessage("[文件]");
                }
            }
            ChatListItemPane itemPane = getChatListItemPane(item);
            itemPanes.add(itemPane);
        }
        chatVBox.getChildren().addAll(itemPanes);

    }

    /**
     * 根据数据渲染聊天列表
     *
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

                if (currentItemId == item.getId()) {
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
            if (currentItemId == item.getId()) {
                return;
            }

            profileUrl = item.getUrl();

            weChatLogo.setVisible(false);
            topNameLabel.setVisible(true);
            chatInfoButton.setVisible(true);
            messageSplitPane.setVisible(true);
            messageSplitPane.setManaged(true);

            messageScrollPane.vvalueProperty().bind(messageVBox.heightProperty());

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
    public void onSendMessage() {
        String text = inputTextArea.getText().trim();
        if (text.equals("")) {
            return;
        }
        User user = userService.getUserById(1);
        String url = user.getUrl();
        Message message = new Message(-1, currentItemId, MessageType.TEXT_MESSAGE_SEND,
                text, null, 0, null, null, url);
        messageService.addMessage(message);
        MessageItemPane messageItemPane = new MessageItemPane(message);
        messageVBox.getChildren().add(messageItemPane);
        inputTextArea.clear();
        messageScrollPane.vvalueProperty().bind(messageVBox.heightProperty());
    }

    /**
     * 加号按钮点击事件
     */
    public void onAddButtonClicked() {
        ChatListItem lastOneItem = chatListItemService.getLastOneItem();
        int id = lastOneItem.getId();
        ChatListItem item = new ChatListItem(id + 1, "/images/profile/wang1.jpeg", "张宇锖", "", "22/2/25");
        chatListItemService.addChatListItem(item);
        ChatListItemPane chatListItemPane = getChatListItemPane(item);
        chatVBox.getChildren().add(chatListItemPane);
    }

    /**
     * 选择文件
     */
    public void onChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("打开");
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("所有文件", "*.*"),
                new FileChooser.ExtensionFilter("图片", "*.png;*.jpg;*.jpeg;*.gif"));
        List<File> files = fileChooser.showOpenMultipleDialog(mainStage);
        if (files != null) {
            for (File file : files) {
                addFileTypeMessage(file);
            }
        }
    }

    /**
     * 添加file类型的消息到消息显示界面，包括图片
     *
     * @param file
     */
    private void addFileTypeMessage(File file) {
        String fileName = file.getName();
        long fileLength = file.length();
        String filePath = file.getAbsolutePath();
        String fileType = fileName.split("\\.")[fileName.split("\\.").length - 1];
        int type;
        if (fileType.equals("jpeg") || fileType.equals("jpg") || fileType.equals("png") ||
                fileType.equals("gif")) {
            type = MessageType.IMAGE_MESSAGE_SEND;
        } else {
            type = MessageType.FILE_MESSAGE_SEND;
        }
        User user = userService.getUserById(1);
        String url = user.getUrl();
        Message message = new Message(-1, currentItemId, type,
                null, fileName, fileLength, fileType, filePath, url);
        messageService.addMessage(message);
        MessageItemPane messageItemPane = new MessageItemPane(message);
        messageVBox.getChildren().add(messageItemPane);
    }

    /**
     * 拖放文件至textarea
     *
     * @param event
     */
    public void onFileDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.MOVE);

    }

    public void onFileDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        List<File> files = db.getFiles();
        if (files.size() > 0) {
            Platform.runLater(() -> {
                FileDialogPane pane = new FileDialogPane(profileUrl, topNameLabel.getText(), files.get(0).getName());
                Scene dialogScene = new Scene(pane);
                Stage dialogStage = new Stage();
                dialogStage.setScene(dialogScene);
                dialogStage.initStyle(StageStyle.UNDECORATED);
                dialogStage.initOwner(mainStage);
                DragWindowHandler handler = new DragWindowHandler(dialogStage);
                pane.setOnMouseDragged(handler);
                pane.setOnMousePressed(handler);

                Label fileNameLabel = (Label) pane.getChildren().get(2);
                Button closeButton = (Button) pane.getChildren().get(4);
                Button sendButton = (Button) pane.getChildren().get(5);
                Button cancelButton = (Button) pane.getChildren().get(6);

                fileNameLabel.setOnMouseClicked(fileNameLabelEvent -> {
                    if (fileNameLabelEvent.getButton() == MouseButton.PRIMARY && fileNameLabelEvent.getClickCount() == 1) {
                        try {
                            Desktop.getDesktop().open(files.get(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                closeButton.setOnMouseClicked(closeButtonEvent -> {
                    if (closeButtonEvent.getButton() == MouseButton.PRIMARY && closeButtonEvent.getClickCount() == 1) {
                        dialogStage.close();
                    }
                });

                cancelButton.setOnMouseClicked(closeButtonEvent -> {
                    if (closeButtonEvent.getButton() == MouseButton.PRIMARY && closeButtonEvent.getClickCount() == 1) {
                        dialogStage.close();
                    }
                });

                sendButton.setOnMouseClicked(sendButtonEvent -> {
                    if (sendButtonEvent.getButton() == MouseButton.PRIMARY && sendButtonEvent.getClickCount() == 1) {
                        addFileTypeMessage(files.get(0));
                        dialogStage.close();
                        messageScrollPane.vvalueProperty().bind(messageVBox.heightProperty());
                    }

                });
                dialogStage.show();
            });

        }
    }

    /**
     * 窗口置顶
     */
    public void onWindowTop() {
        if (mainStage.isAlwaysOnTop()) {
            topButton.setStyle("-fx-background-image: url(/images/button/pin.png)");
            mainStage.setAlwaysOnTop(false);

        } else {
            topButton.setStyle("-fx-background-image: url(/images/button/is_top.png)");
            mainStage.setAlwaysOnTop(true);
        }
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
        if (mainStage.isMaximized()) {
            maximizeButton.setStyle("-fx-background-image: url(/images/button/enlarge.png)");
            mainStage.setMaximized(false);

        } else {
            maximizeButton.setStyle("-fx-background-image: url(/images/button/full_screen.png);");
            mainStage.setMaximized(true);
        }
    }

    /**
     * 窗口关闭
     */
    public void onWindowClosed() {
        mainStage.close();
    }
}
