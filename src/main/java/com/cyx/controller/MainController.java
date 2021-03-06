package com.cyx.controller;


import com.cyx.ClientApplication;
import com.cyx.component.*;
import com.cyx.constant.LoginState;
import com.cyx.constant.MessageType;
import com.cyx.constant.Port;
import com.cyx.handler.DragWindowHandler;
import com.cyx.pojo.*;
import com.cyx.service.*;
import com.cyx.task.ReceiveTask;
import com.cyx.task.SendTask;
import com.cyx.utils.DateUtils;
import com.cyx.utils.FileUtils;
import com.google.gson.Gson;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Arc;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.*;
import java.util.List;

@FXMLController
public class MainController implements Initializable {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ChatListItemServiceImpl chatListItemService;

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private FriendServiceImpl friendService;

    private Stage mainStage;

    @FXML
    private AnchorPane mainView;

    @FXML
    private ImageView currentUserProfile;

    @FXML
    private AnchorPane middleAnchorPane;

    @FXML
    private AnchorPane rightAnchorPane;

    @FXML
    private ScrollPane chatListScrollPane;

    @FXML
    private SplitPane messageSplitPane;

    @FXML
    private VBox chatVBox;

    @FXML
    private VBox messageVBox;

    @FXML
    private VBox moreVBox;

    @FXML
    private Label topNameLabel;

    @FXML
    private Button chatInfoButton;

    @FXML
    private Button topButton;

    @FXML
    private Button chatRecordButton;

    @FXML
    private Button chatButton;

    @FXML
    private Button addressListButton;

    @FXML
    private Button maximizeButton;

    @FXML
    private Button moreButton;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextField searchField;

    @FXML
    private HBox searchHBox;

    @FXML
    private ImageView weChatLogo;

    @FXML
    private ScrollPane messageScrollPane;

    @FXML
    private Label chatCountsLabel;

    private final List<ChatListItem> currentChatListItems = new ArrayList<>();

    private HashMap<Integer, MessageItemPane> messageItemPaneMap = new HashMap<>();

    private Friend currentFriend;

    private AddressListVBox addressListVBox;

    private AnchorPane friendInfoPane;

    private AnchorPane addFriendPane;

    private AnchorPane newFriendPane;

    private String addFriendPaneProfileUrl;

    private String profileUrl;

    private HashMap<String, Node> friendInfoNodesMap;

    private boolean isSettingsViewShow = false;

    int currentItemId = -1;

    private User currentUser;

    private ServerSocket serverSocket;

    private Socket currentSocket;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            initView();
            initReceiveProgress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initReceiveProgress() {
        new Thread(() -> {
            try {
                if (currentUser.getUsername().equals("alice")) {
                    serverSocket = new ServerSocket(Port.RECEIVE_PORT_ALICE);
                } else {
                    serverSocket = new ServerSocket(Port.RECEIVE_PORT_BOB);
                }

                while (true) {
                    currentSocket = serverSocket.accept();
                    ReceiveTask receiverTask = new ReceiveTask(currentSocket);
                    receiverTask.valueProperty().addListener((observable, oldValue, progressMsg) -> {

                        String filename = progressMsg.getFileName();
                        String senderName = progressMsg.getSenderName();
                        int identification = progressMsg.getIdentification();
                        long fileLength = progressMsg.getFileLength();
                        String fileType = filename.split("\\.")[filename.split("\\.").length - 1];
                        String filePath = "D:\\IdeaProjects\\DemoClient\\workplace\\" + currentUser.getUsername() + "\\restored\\" + identification + "\\" + filename;
                        String url = "/images/logo/logo.png";
                        Friend friend = friendService.getFriendByUserIdAndUsername(currentUser.getId(), senderName);
                        if (friend != null) {
                            url = friend.getUrl();
                        }

                        if (!messageItemPaneMap.containsKey(identification) && new File(filePath).exists()) {
                            return;
                        }

                        if (progressMsg.getMsgType().equals("receiveProgress")) {

                            if (!messageItemPaneMap.containsKey(identification)) {

                                if (friend != null) {
                                    currentFriend = friend;
                                    addNewChatListItem(friend);
                                }

                                int type;
                                if (FileUtils.getFileType(filename).equals("file")) {
                                    type = MessageType.FILE_MESSAGE_RECEIVE;
                                } else {
                                    type = MessageType.IMAGE_MESSAGE_RECEIVE;
                                }

                                Message message = new Message(-1, currentItemId, type,
                                        null, filename, fileLength, fileType, filePath, url);
                                messageService.addMessage(message);
                                Message lastMessage = messageService.getLastMessageByChatItemId(currentItemId);
                                message.setId(lastMessage.getId());
                                MessageItemPane messageItemPane = new MessageItemPane(message);
                                messageItemPaneMap.put(identification, messageItemPane);

                                if (type == MessageType.FILE_MESSAGE_RECEIVE) {
                                    messageVBox.getChildren().add(messageItemPane);
                                }

                                weChatLogo.setVisible(false);
                                topNameLabel.setVisible(true);
                                chatInfoButton.setVisible(true);
                                messageSplitPane.setVisible(true);
                                messageSplitPane.setManaged(true);

                                topNameLabel.setText(currentFriend.getUsername());
                                chatButton.setStyle("-fx-background-image: url(/images/button/bubble_focus.png)");
                                addressListButton.setStyle("-fx-background-image: url(/images/button/album.png)");
                            }

                        }

                        if (FileUtils.getFileType(filename).equals("file")) {
                            MessageItemPane messageItemPane = messageItemPaneMap.get(progressMsg.getIdentification());
                            AnchorPane filePane = (AnchorPane) messageItemPane.getChildren().get(0);
                            ImageView fileImage = (ImageView) filePane.getChildren().get(1);
                            AnchorPane progressPane = (AnchorPane) filePane.getChildren().get(0);
                            Label metaLabel = (Label) filePane.getChildren().get(4);
                            Arc arc = (Arc) progressPane.getChildren().get(2);
                            arc.setLength(progressMsg.getPercentage() / 100f * 360);
                            if (progressMsg.getPercentage() >= 100) {
                                fileImage.setVisible(true);
                                fileImage.setManaged(true);
                                progressPane.setVisible(false);
                                progressPane.setManaged(false);
                                metaLabel.setText("???????????????");
                                messageItemPaneMap.remove(progressMsg.getIdentification());
                            } else {
                                fileImage.setVisible(false);
                                fileImage.setManaged(false);
                                progressPane.setVisible(true);
                                progressPane.setManaged(true);
                            }
                        } else {
                            if (progressMsg.getPercentage() >= 100) {
                                MessageItemPane messageItemPane = messageItemPaneMap.get(progressMsg.getIdentification());
                                if (new File(filePath).exists() && messageItemPane != null) {
                                    AnchorPane imagePane = (AnchorPane) messageItemPane.getChildren().get(0);
                                    ImageView imageView = (ImageView) imagePane.getChildren().get(0);
                                    imageView.setImage(new Image("file:" + filePath));
                                    messageItemPane.setVisible(true);
                                    messageItemPane.setManaged(true);
                                    messageVBox.getChildren().add(messageItemPane);
                                    messageItemPaneMap.remove(progressMsg.getIdentification());
                                }

                            }
                        }


                    });
                    new Thread(receiverTask).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }

    /**
     * ?????????????????????
     */
    public void initView() throws IOException {

        currentUser = userService.getUserByLoginState(LoginState.IS_LOGIN);

        currentUserProfile.setImage(new Image("file:" + currentUser.getUrl()));

        mainStage = ClientApplication.getStage();

        DragWindowHandler handler = new DragWindowHandler(mainStage);
        mainView.setOnMousePressed(handler);
        mainView.setOnMouseDragged(handler);

        friendInfoPane = initRightPane("/fxml/FriendInfoView.fxml");
        friendInfoNodesMap = getNodesMapFromFriendInfoPane(friendInfoPane);

        addFriendPane = initRightPane("/fxml/AddFriendView.fxml");
        initAddFriendPaneNodesEvent(addFriendPane);

        messageSplitPane.setVisible(false);
        messageSplitPane.setManaged(false);
        topNameLabel.setVisible(false);
        chatInfoButton.setVisible(false);
        weChatLogo.setVisible(true);

        messageVBox.prefWidthProperty().bind(messageSplitPane.widthProperty());

        List<ChatListItem> allChatListItems = chatListItemService.getChatListItemsByUserId(currentUser.getId());
        currentChatListItems.addAll(allChatListItems);
        List<ChatListItemPane> itemPanes = new ArrayList<>();
        for (ChatListItem item : allChatListItems) {
            Message lastMessage = messageService.getLastMessageByChatItemId(item.getId());
            if (lastMessage == null) {
                item.setMessage("");
            } else {
                if (lastMessage.getType() == MessageType.TEXT_MESSAGE_SEND ||
                        lastMessage.getType() == MessageType.TEXT_MESSAGE_RECEIVE) {
                    item.setMessage(lastMessage.getText());
                } else if (lastMessage.getType() == MessageType.IMAGE_MESSAGE_SEND ||
                        lastMessage.getType() == MessageType.IMAGE_MESSAGE_RECEIVE) {
                    item.setMessage("[??????]");
                } else {
                    item.setMessage("[??????]");
                }
            }
            ChatListItemPane itemPane = getChatListItemPane(item);
            itemPanes.add(itemPane);
        }
        chatVBox.getChildren().addAll(itemPanes);

        if (allChatListItems.size() == 0) {
            chatCountsLabel.setVisible(false);
        } else {
            chatCountsLabel.setVisible(true);
            chatCountsLabel.setText(String.valueOf(allChatListItems.size()));
        }


        List<Friend> friendList = friendService.getAllFriendsByUserId(currentUser.getId());
        List<FriendListItemPane> friendListItemPanes = new ArrayList<>();
        for (Friend friend : friendList) {
            String url = friend.getUrl();
            String username = friend.getUsername();
            FriendListItemPane friendListItemPane = new FriendListItemPane(url, username);
            friendListItemPane.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                    showFriendInfoPane(friend);
                }
            });
            friendListItemPanes.add(friendListItemPane);
        }
        addressListVBox = new AddressListVBox(friendListItemPanes);

        newFriendPane = (AnchorPane) addressListVBox.getChildren().get(2);
        newFriendPane.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                showAddFriendPane();
            }
        });

        searchField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                searchField.setStyle("-fx-background-color: white");
                searchHBox.setStyle("-fx-background-color: white;" +
                        "-fx-border-style: solid;" +
                        "-fx-border-color: #e6e6e6");

                SOSPane sosPane = new SOSPane();
                chatListScrollPane.setContent(sosPane);


            } else {
                searchField.setStyle("-fx-background-color: #e2e2e2");
                searchHBox.setStyle("-fx-background-color: #e2e2e2;" +
                        "-fx-border-style: none");
                chatListScrollPane.setContent(chatVBox);
            }
        });

        for (Node child : moreVBox.getChildren()) {
            child.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    child.setStyle("-fx-background-color: #353535");
                } else {
                    child.setStyle("-fx-background-color: #2e2e2e");
                }
            });
        }

        Label settingsLabel = (Label) moreVBox.getChildren().get(2);
        settingsLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                if (!isSettingsViewShow) {
                    showSettingsView();
                    isSettingsViewShow = true;
                    moreVBox.setVisible(false);
                    moreVBox.setManaged(false);
                }
            }
        });

        moreVBox.setVisible(false);
        moreVBox.setManaged(false);

    }

    /**
     * ??????FXML????????????????????????Pane
     *
     * @throws IOException
     */
    private AnchorPane initRightPane(String fxmlPath) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(fxmlPath));
        rightAnchorPane.getChildren().add(anchorPane);
        AnchorPane.setTopAnchor(anchorPane, 62.0);
        AnchorPane.setLeftAnchor(anchorPane, 0.0);
        AnchorPane.setRightAnchor(anchorPane, 0.0);
        AnchorPane.setBottomAnchor(anchorPane, 0.0);
        anchorPane.setVisible(false);
        anchorPane.setManaged(false);
        return anchorPane;
    }

    /**
     * ??????FriendInfoPane??????????????????
     *
     * @param friendInfoPane
     * @return
     */
    private HashMap<String, Node> getNodesMapFromFriendInfoPane(AnchorPane friendInfoPane) {
        HashMap<String, Node> nodeMap = new HashMap<>();
        for (Node child : friendInfoPane.getChildren()) {

            if (child.getId() != null) {

                switch (child.getId()) {
                    case "usernameHBox":
                        nodeMap.put("usernameLabel", ((HBox) child).getChildren().get(0));
                        nodeMap.put("sexIV", ((HBox) child).getChildren().get(1));
                        break;
                    case "sign":
                        nodeMap.put("signLabel", child);
                        break;
                    case "profile":
                        nodeMap.put("profileIV", child);
                        break;
                    case "remark":
                        nodeMap.put("remarkLabel", child);
                        break;
                    case "region":
                        nodeMap.put("regionLabel", child);
                        break;
                    case "weChatAccount":
                        nodeMap.put("weChatAccountLabel", child);
                        break;
                    case "from":
                        nodeMap.put("fromLabel", child);
                        break;
                    case "sendMessageButton":
                        child.hoverProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                child.setStyle("-fx-background-color: #129611");
                            } else {
                                child.setStyle("-fx-background-color:  #1aad19");
                            }
                        });
                        child.setOnMouseClicked(event -> {

                            chatListScrollPane.setContent(chatVBox);

                            addNewChatListItem(currentFriend);

                            Platform.runLater(() -> {
                                renderMessagesOnViewByChatItemId(currentItemId);
                                topNameLabel.setText(currentFriend.getUsername());

                            });

                            chatButton.setStyle("-fx-background-image: url(/images/button/bubble_focus.png)");
                            addressListButton.setStyle("-fx-background-image: url(/images/button/album.png)");
                            showChatView();

                        });
                        break;
                    default:
                        nodeMap.put("others", child);
                }
            }
        }
        return nodeMap;
    }

    /**
     * ?????????AddFriendPane???????????????
     */
    private void initAddFriendPaneNodesEvent(AnchorPane addFriendPane) {
        ImageView profile = (ImageView) addFriendPane.getChildren().get(0);
        TextField usernameTF = (TextField) addFriendPane.getChildren().get(1);
        Label sexLabel = (Label) addFriendPane.getChildren().get(2);
        TextField signTF = (TextField) addFriendPane.getChildren().get(3);
        TextField remarkTF = (TextField) addFriendPane.getChildren().get(4);
        TextField regionTF = (TextField) addFriendPane.getChildren().get(5);
        Label weChatLabel = (Label) addFriendPane.getChildren().get(6);
        Label fromLabel = (Label) addFriendPane.getChildren().get(7);
        TextField publicKeyTF = (TextField) addFriendPane.getChildren().get(8);
        Button selectButton = (Button) addFriendPane.getChildren().get(9);
        Button addButton = (Button) addFriendPane.getChildren().get(10);

        addFriendPaneProfileUrl = "/images/profile/wang" + new Random().nextInt(5) + ".jpeg";
        profile.setImage(new Image(addFriendPaneProfileUrl));

        sexLabel.setText(new Random().nextInt(2) == 1 ? "???" : "???");

        weChatLabel.setText(RandomStringUtils.randomAlphanumeric(10));

        fromLabel.setText("??????????????????");

        profile.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                addFriendPaneProfileUrl = "/images/profile/wang" + new Random().nextInt(5) + ".jpeg";
                profile.setImage(new Image(addFriendPaneProfileUrl));
            }
        });

        sexLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                sexLabel.setText(new Random().nextInt(2) == 1 ? "???" : "???");
            }
        });

        weChatLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                weChatLabel.setText(RandomStringUtils.randomAlphanumeric(10));
            }
        });

        selectButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("??????");
                fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("????????????", "*.pub;*pem"));
                List<File> files = fileChooser.showOpenMultipleDialog(mainStage);
                if (files != null) {
                    publicKeyTF.setText(files.get(0).getAbsolutePath());
                }
            }
        });

        addButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                if (usernameTF.getText().trim().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("??????");
                    alert.setHeaderText("??????????????????");
                    alert.initOwner(mainStage);
                    alert.showAndWait();
                } else {


                    Friend friend = new Friend(-1, currentUser.getId(), usernameTF.getText(), addFriendPaneProfileUrl,
                            sexLabel.getText().equals("???") ? 1 : 0, signTF.getText(), remarkTF.getText(),
                            regionTF.getText(), weChatLabel.getText(), fromLabel.getText(),
                            publicKeyTF.getText());

                    friendService.addFriend(friend);
                    Friend lastFriend = friendService.getLastFriendByUserId(currentUser.getId());
                    friend.setId(lastFriend.getId());

                    FriendListItemPane friendListItemPane = new FriendListItemPane(addFriendPaneProfileUrl, usernameTF.getText());
                    friendListItemPane.setOnMouseClicked(event1 -> {
                        if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                            currentFriend = friend;
                            showFriendInfoPane(friend);
                        }
                    });
                    addressListVBox.getChildren().add(friendListItemPane);
                }
            }
        });
    }


    /**
     * ?????????ChatList????????????friend?????????ChatItem
     *
     * @param friend
     */
    private void addNewChatListItem(Friend friend) {
        int chatItemId = getChatItemIdIfFriendInChatList(friend);
        if (chatItemId != -1) {
            currentItemId = chatItemId;
        } else {
            ChatListItem newChatListItem = new ChatListItem(-1, currentUser.getId(), currentFriend.getId(),
                    currentFriend.getUrl(), currentFriend.getUsername(), "", DateUtils.currentTime());
            chatListItemService.addChatListItem(newChatListItem);
            ChatListItem lastChatListItem = chatListItemService.getLastOneItemByUserId(currentUser.getId());
            newChatListItem.setId(lastChatListItem.getId());
            currentChatListItems.add(newChatListItem);
            chatVBox.getChildren().add(getChatListItemPane(newChatListItem));
            currentItemId = newChatListItem.getId();
            chatCountsLabel.setVisible(true);
            chatCountsLabel.setText(String.valueOf(currentChatListItems.size()));
        }
    }

    /**
     * ??????????????????????????????????????????
     *
     * @return
     */
    private int getChatItemIdIfFriendInChatList(Friend friend) {
        int index = 0;
        for (; index < currentChatListItems.size(); index++) {
            if (currentChatListItems.get(index).getFriendId() == friend.getId()) {
                break;
            }
        }
        if (index == currentChatListItems.size()) {
            return -1;
        } else {
            return currentChatListItems.get(index).getId();
        }
    }

    private void showChatView() {
        if (chatVBox.getChildren().size() == 0) {
            weChatLogo.setVisible(true);
            weChatLogo.setManaged(true);
            messageSplitPane.setVisible(false);
            messageSplitPane.setManaged(false);
            chatRecordButton.setVisible(false);
            chatRecordButton.setManaged(false);
            topNameLabel.setVisible(false);
            topNameLabel.setManaged(false);
        } else {
            weChatLogo.setVisible(false);
            weChatLogo.setManaged(false);
            messageSplitPane.setVisible(true);
            messageSplitPane.setManaged(true);
            chatRecordButton.setVisible(true);
            chatRecordButton.setManaged(true);
            topNameLabel.setVisible(true);
            topNameLabel.setManaged(true);
        }
        friendInfoPane.setVisible(false);
        friendInfoPane.setManaged(false);
        addFriendPane.setVisible(false);
        addFriendPane.setManaged(false);
    }

    private void showAddFriendPane() {
        friendInfoPane.setVisible(false);
        friendInfoPane.setManaged(false);
        addFriendPane.setVisible(true);
        addFriendPane.setManaged(true);
        messageSplitPane.setVisible(false);
        messageSplitPane.setManaged(false);
        chatRecordButton.setVisible(false);
        chatRecordButton.setManaged(false);
        topNameLabel.setVisible(false);
        topNameLabel.setManaged(false);
        weChatLogo.setVisible(false);
        weChatLogo.setManaged(false);
    }

    private void showFriendInfoPane(Friend friend) {
        messageSplitPane.setVisible(false);
        messageSplitPane.setManaged(false);
        chatRecordButton.setVisible(false);
        chatRecordButton.setManaged(false);
        weChatLogo.setVisible(false);
        weChatLogo.setManaged(false);
        topNameLabel.setVisible(false);
        topNameLabel.setManaged(false);
        addFriendPane.setVisible(false);
        addFriendPane.setManaged(false);
        friendInfoPane.setVisible(true);
        friendInfoPane.setManaged(true);

        currentFriend = friend;

        ((Label) friendInfoNodesMap.get("usernameLabel")).setText(friend.getUsername());
        ((Label) friendInfoNodesMap.get("signLabel")).setText(friend.getSign());
        String sexImageUrl;
        if (friend.getSex() == 0) {
            sexImageUrl = "/images/logo/profile_female.png";
        } else {
            sexImageUrl = "/images/logo/profile_male.png";
        }
        ((ImageView) friendInfoNodesMap.get("sexIV")).setImage(new Image(sexImageUrl));
        ((ImageView) friendInfoNodesMap.get("profileIV")).setImage(new Image(friend.getUrl()));
        ((Label) friendInfoNodesMap.get("remarkLabel")).setText(friend.getRemark());
        ((Label) friendInfoNodesMap.get("regionLabel")).setText(friend.getRegion());
        ((Label) friendInfoNodesMap.get("weChatAccountLabel")).setText(friend.getWeChatAccount());
        ((Label) friendInfoNodesMap.get("fromLabel")).setText(friend.getOrigin());
    }

    /**
     * ??????????????????
     */
    private void showSettingsView() {

        Stage settingsStage = new Stage();
        AnchorPane settingsPane = new SettingsPane(mainStage, currentUser, userService);
        Scene settingsScene = new Scene(settingsPane);
        settingsStage.setScene(settingsScene);
        settingsStage.initOwner(mainStage);
        settingsStage.initStyle(StageStyle.UNDECORATED);
        DragWindowHandler handler = new DragWindowHandler(settingsStage);
        settingsPane.setOnMouseDragged(handler);
        settingsPane.setOnMousePressed(handler);

        Button minimizeButton = ((Button) settingsPane.getChildren().get(1));
        minimizeButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                settingsStage.setIconified(true);
            }
        });

        Button closeButton = ((Button) settingsPane.getChildren().get(2));
        closeButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                settingsStage.close();
                isSettingsViewShow = false;
            }
        });

        settingsStage.show();

    }

    /**
     * ??????chatItemId?????????????????????????????????????????????????????????
     */
    private void renderMessagesOnViewByChatItemId(int chatItemId) {
        messageVBox.getChildren().removeAll(messageVBox.getChildren());
        List<Message> messages = messageService.getMessagesByChatItemId(chatItemId);
        List<MessageItemPane> messageItemPanes = new ArrayList<>();
        for (Message message : messages) {
            MessageItemPane messageItemPane = new MessageItemPane(message);
            if (message.getType() == MessageType.TEXT_MESSAGE_SEND ||
                    message.getType() == MessageType.TEXT_MESSAGE_RECEIVE) {
                Label msgLabel = (Label) messageItemPane.getChildren().get(1);
                msgLabel.maxWidthProperty().bind(rightAnchorPane.widthProperty().multiply(0.55));

            } else if (message.getType() == MessageType.FILE_MESSAGE_SEND ||
                    message.getType() == MessageType.FILE_MESSAGE_RECEIVE) {
                AnchorPane filePane = (AnchorPane) messageItemPane.getChildren().get(0);
                ImageView fileImage = (ImageView) filePane.getChildren().get(1);
                AnchorPane progressPane = (AnchorPane) filePane.getChildren().get(0);
                Label metaLabel = (Label) filePane.getChildren().get(4);
                metaLabel.setText("???????????????");
                progressPane.setVisible(false);
                progressPane.setManaged(false);
                fileImage.setVisible(true);
                fileImage.setManaged(true);
            }else{
                messageItemPane.setVisible(true);
                messageItemPane.setManaged(true);
            }
            messageItemPanes.add(messageItemPane);
        }
        System.out.println(messages);
        messageVBox.getChildren().addAll(messageItemPanes);
    }

    /**
     * ??????????????????????????????
     *
     * @param item ?????????????????????
     * @return ???????????????
     */
    private ChatListItemPane getChatListItemPane(ChatListItem item) {
        ChatListItemPane itemPane = new ChatListItemPane(item);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem topItem = new MenuItem("??????");
        MenuItem readItem = new MenuItem("????????????");
        MenuItem botherItem = new MenuItem("???????????????");
        MenuItem windowItem = new MenuItem("????????????????????????");
        MenuItem visibleItem = new MenuItem("???????????????");
        MenuItem deleteItem = new MenuItem("????????????");
        contextMenu.getItems().addAll(topItem, readItem, botherItem, windowItem,
                visibleItem, deleteItem);

        deleteItem.setOnAction(event -> {
            chatVBox.getChildren().remove(itemPane);
            if (currentItemId == item.getId()) {
                messageSplitPane.setVisible(false);
                topNameLabel.setVisible(false);
                chatInfoButton.setVisible(false);
                weChatLogo.setVisible(true);
            }

            chatListItemService.deleteChatListItemById(item.getId());
            messageService.deleteMessagesByChatItemId(item.getId());
            currentChatListItems.remove(item);
            if (currentChatListItems.size() == 0) {
                chatCountsLabel.setVisible(false);
            } else {
                chatCountsLabel.setText(String.valueOf(currentChatListItems.size()));
            }

        });

        itemPane.setOnContextMenuRequested(event -> {
            System.out.println(item.getId());
            contextMenu.show(itemPane, event.getScreenX(), event.getScreenY());
        });

        itemPane.setOnMouseClicked(event -> {
            if (currentItemId == item.getId() || !(event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1)) {
                return;
            }

            profileUrl = item.getUrl();

            currentItemId = item.getId();

            weChatLogo.setVisible(false);
            topNameLabel.setVisible(true);
            chatInfoButton.setVisible(true);
            messageSplitPane.setVisible(true);
            messageSplitPane.setManaged(true);

            messageScrollPane.vvalueProperty().bind(messageVBox.heightProperty());

            // ??????????????????????????????????????????????????????????????????
            Platform.runLater(() -> {

                renderMessagesOnViewByChatItemId(currentItemId);
                topNameLabel.setText(item.getUsername());

            });

        });
        return itemPane;
    }

    /**
     * ????????????????????????
     */
    public void onSendMessage() {
        String text = inputTextArea.getText().trim();
        if (text.equals("")) {
            return;
        }

        String url = currentUser.getUrl();

        Message message = new Message(-1, currentItemId, MessageType.TEXT_MESSAGE_SEND,
                text, null, 0, null, null, url);
        messageService.addMessage(message);
        Message lastMessage = messageService.getLastMessageByChatItemId(currentItemId);
        message.setId(lastMessage.getId());
        MessageItemPane messageItemPane = new MessageItemPane(message);
        messageVBox.getChildren().add(messageItemPane);
        inputTextArea.clear();
        messageScrollPane.vvalueProperty().bind(messageVBox.heightProperty());
    }

    /**
     * ????????????????????????
     */
    public void onAddButtonClicked() {

    }

    /**
     * ????????????????????????
     */
    public void onChatButtonClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
            chatListScrollPane.setContent(chatVBox);
            chatButton.setStyle("-fx-background-image: url(/images/button/bubble_focus.png)");
            addressListButton.setStyle("-fx-background-image: url(/images/button/album.png)");
            if (weChatLogo.isVisible()) {
                return;
            }
            showChatView();

        }
    }

    /**
     * ???????????????????????????
     */
    public void onAddressListButtonClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
            chatListScrollPane.setContent(addressListVBox);
            addressListButton.setStyle("-fx-background-image: url(/images/button/album_focus.png)");
            chatButton.setStyle("-fx-background-image: url(/images/button/bubble.png)");
        }
    }

    private boolean isMoreVBoxShow = false;

    /**
     * ????????????????????????
     */
    public void onMoreButtonClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
            if (isMoreVBoxShow) {
                moreVBox.setVisible(false);
                moreVBox.setManaged(false);
                isMoreVBoxShow = false;
            } else {
                moreVBox.setVisible(true);
                moreVBox.setManaged(true);
                isMoreVBoxShow = true;
            }
        }
    }

    /**
     * ????????????
     */
    public void onChooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("??????");
        fileChooser.setInitialDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("????????????", "*.*"),
                new FileChooser.ExtensionFilter("??????", "*.png;*.jpg;*.jpeg;*.gif"));
        File file = fileChooser.showOpenDialog(mainStage);
        if (file != null) {
            int identification = new Random().nextInt(999999999);
            SendStrategy sendStrategy = new SendStrategy("send_strategy", identification, currentUser.getUsername(), topNameLabel.getText().trim(),
                    file.getAbsolutePath(), file.length(), 8, 3, 1000);
            String info = new Gson().toJson(sendStrategy);
            if (currentUser.getUsername().equals("alice")) {
                SendTask.send(Port.SEND_PORT_ALICE, info);
            } else {
                SendTask.send(Port.SEND_PORT_BOB, info);
            }

            addFileTypeMessage(file, identification);
        }
    }

    /**
     * ??????file???????????????????????????????????????????????????
     *
     * @param file
     */
    private void addFileTypeMessage(File file, int identification) {
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

        String url = currentUser.getUrl();
        Message message = new Message(-1, currentItemId, type,
                null, fileName, fileLength, fileType, filePath, url);
        messageService.addMessage(message);
        Message lastMessage = messageService.getLastMessageByChatItemId(currentItemId);
        message.setId(lastMessage.getId());
        MessageItemPane messageItemPane = new MessageItemPane(message);
        if (type == MessageType.IMAGE_MESSAGE_SEND) {
            messageItemPane.setVisible(true);
            messageItemPane.setManaged(true);
        }
        messageVBox.getChildren().add(messageItemPane);
        if (type == MessageType.FILE_MESSAGE_SEND) {
            messageItemPaneMap.put(identification, messageItemPane);
        }
    }

    /**
     * ???????????????textarea
     *
     * @param event
     */
    public void onFileDragOver(DragEvent event) {
        event.acceptTransferModes(TransferMode.MOVE);

    }

    /**
     * ????????????????????????
     *
     * @param event
     */
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
                        int identification = new Random().nextInt(999999999);
                        SendStrategy sendStrategy = new SendStrategy("send_strategy", identification, currentUser.getUsername(), topNameLabel.getText().trim(),
                                files.get(0).getAbsolutePath(), files.get(0).length(), 8, 3, 1000);
                        String info = new Gson().toJson(sendStrategy);
                        if (currentUser.getUsername().equals("alice")) {
                            SendTask.send(Port.SEND_PORT_ALICE, info);
                        } else {
                            SendTask.send(Port.SEND_PORT_BOB, info);
                        }
                        addFileTypeMessage(files.get(0), identification);
                        dialogStage.close();
                        messageScrollPane.vvalueProperty().bind(messageVBox.heightProperty());
                    }

                });
                dialogStage.show();
            });

        }
    }


    /**
     * ????????????
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
     * ????????????
     */
    public void onWindowHide() {
        mainStage.setIconified(true);
    }

    /**
     * ????????????
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
     * ????????????
     */
    public void onWindowClosed() {
        currentUser.setLoginState(LoginState.NOT_LOGIN);
        userService.updateUserByUsername(currentUser);
        System.exit(0);
        mainStage.close();
    }
}
