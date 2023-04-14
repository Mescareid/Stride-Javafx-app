package com.example.networkfx;

import com.example.networkfx.config.Credentials;
import com.example.networkfx.domain.Friendship;
import com.example.networkfx.domain.FriendshipStatus;
import com.example.networkfx.domain.Message;
import com.example.networkfx.domain.User;
import com.example.networkfx.domain.validators.MessageValidator;
import com.example.networkfx.repository.Repository;
import com.example.networkfx.repository.database.MessageDatabaseRepository;
import com.example.networkfx.service.ServiceFriendship;
import com.example.networkfx.service.ServiceMessage;
import com.example.networkfx.service.ServiceUser;
import com.example.networkfx.utils.events.FriendEntityChangeEvent;
import com.example.networkfx.utils.observer.Observer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.xml.transform.Source;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppController implements Initializable, Observer<FriendEntityChangeEvent> {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String user;
    private User userEntity;
    private User selectedUser;
    private Friendship selectedFriendship;

    private ServiceUser serviceUser;
    private ServiceFriendship serviceFriendship;
    private ServiceMessage serviceMessage;
    private List<User> friends = new ArrayList<>();
    private final ObservableList<String> friendsModel = FXCollections.observableArrayList();
    private final ObservableList<String> searchListResults = FXCollections.observableArrayList();

    private List<User> usersList = new ArrayList<>();
    private List<String> usersForSearchList = new ArrayList<>();
    @FXML
    private ImageView avatar = new ImageView();
    @FXML
    private Button logOutButton;
    @FXML
    private Button friendRequestButton;
    @FXML
    private Button removeFriendButton;
    @FXML
    private Button chatButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button cancelRequestButton;
    @FXML
    private Button sendMessageButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button acceptFriendRequest;
    @FXML
    private Button rejectFriendButton;
    @FXML
    private Label firstName;
    @FXML
    private Label numberOfFriends;
    @FXML
    private Label numberOfRequests;
    @FXML
    private Label lastName;
    @FXML
    private Label username;
    @FXML
    private Label chatIdle;
    @FXML
    private TextField searchBar;
    @FXML
    private TextField messageBar;
    @FXML
    private ListView<String> searchResults;
    @FXML
    private ListView<String> myFriends;
    @FXML
    private ListView<String> chatMessages;
    @FXML
    private TableView<Friendship> friendRequestsTable;
    @FXML
    private TableColumn<Friendship, String> fromColumn = new TableColumn<>("From");
    @FXML
    private TableColumn<Friendship, String> toColumn = new TableColumn<>("To");
    @FXML
    private TableColumn<Friendship, FriendshipStatus> statusColumn = new TableColumn<>("Status");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fromColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getUser1().getUsername()));
        toColumn.setCellValueFactory(c-> new SimpleStringProperty(c.getValue().getUser2().getUsername()));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        System.out.println("Reached app phase");
    }

    @FXML
    private void displaySelected(MouseEvent event){
        selectedFriendship = friendRequestsTable.getSelectionModel().getSelectedItem();
        if(selectedFriendship.getStatus() == FriendshipStatus.PENDING){
            if(selectedFriendship.getUser1().getUsername().equals(user)){
                deleteButton.setDisable(true);
                cancelRequestButton.setDisable(false);
                acceptFriendRequest.setDisable(true);
                rejectFriendButton.setDisable(true);
            }
            if(selectedFriendship.getUser2().getUsername().equals(user)){
                deleteButton.setDisable(true);
                cancelRequestButton.setDisable(true);
                acceptFriendRequest.setDisable(false);
                rejectFriendButton.setDisable(false);
            }
        }
        if(selectedFriendship.getStatus() == FriendshipStatus.CANCELED ||
                selectedFriendship.getStatus() == FriendshipStatus.REJECTED){
            acceptFriendRequest.setDisable(true);
            cancelRequestButton.setDisable(true);
            deleteButton.setDisable(false);
            rejectFriendButton.setDisable(true);
        }
    }

    public void setUser(String username) {
        user = username;
        userEntity = serviceUser.findUser(user);
        selectedUser = userEntity;
    }

    public void setServices(ServiceUser serviceUser, ServiceFriendship serviceFriendship){
        this.serviceUser = serviceUser;
        this.serviceFriendship = serviceFriendship;
        serviceFriendship.addObserver(this);
    }

    @Override
    public void update(FriendEntityChangeEvent friendEntityChangeEvent){
        initModel();
    }

    private void setSelectedUser(String text){
        if(text == null){
            return;
        }
        String[] parts = text.split("\\(");
        selectedUser.setUsername(parts[0]);
        username.setText(parts[0]);

        String[] parts1 = parts[1].split(" ");
        selectedUser.setFirstName(parts1[0]);
        firstName.setText(parts1[0]);

        String res = parts1[1].replace(")", "");
        selectedUser.setLastName(res);
        long aux = selectedUser.getId() + 1;
        selectedUser.setId(aux);
        lastName.setText(res);

        if(user.equals(parts[0])){
            removeFriendButton.setDisable(true);
            chatButton.setDisable(true);
            friendRequestButton.setDisable(true);
            return;
        }

        if(isFriend(parts[0])){
            friendRequestButton.setDisable(true);
            removeFriendButton.setDisable(false);
            chatButton.setDisable(false);
        }
        else {
            removeFriendButton.setDisable(true);
            chatButton.setDisable(true);
            friendRequestButton.setDisable(false);
        }

        chatMessages.getItems().clear();
        chatIdle.setText("Select a friend to start chatting");
        messageBar.clear();

        long avatarNumber = aux % 13;
        Image userAvatar = new Image("/avatars/avatar" + avatarNumber + ".png");
        avatar.setImage(userAvatar);
    }

    public List<String> extractFriendsForFriendList(List<Friendship> friendships){
        friends.clear();
        List<String> formattedList = new ArrayList<>();
        for(Friendship friendship : friendships){
            if(friendship.getUser1().getUsername().equals(user) && friendship.getStatus() == FriendshipStatus.ACCEPTED){
                User friend = friendship.getUser2();
                formattedList.add(friend.getUsername() + '(' + friend.getFirstName() + ' ' + friend.getLastName() + ')');
                friends.add(friend);
            }
            if(friendship.getUser2().getUsername().equals(user) && friendship.getStatus() == FriendshipStatus.ACCEPTED){
                User friend = friendship.getUser1();
                formattedList.add(friend.getUsername() + '(' + friend.getFirstName() + ' ' + friend.getLastName() + ')');
                friends.add(friend);
            }
        }
        return formattedList;
    }

    public List<String> extractDataForSearchResults(List<User> users){
        List<String> formattedList = new ArrayList<>();
        for(User u : users){
            formattedList.add(u.getUsername() + '(' + u.getFirstName() + ' ' + u.getLastName() + ')');
            usersForSearchList.add(u.getUsername() + '(' + u.getFirstName() + ' ' + u.getLastName() + ')');
        }
        return formattedList;
    }

    private boolean isFriend(String username){
        for(User u : friends){
            if(u.getUsername().equals(username) )
                return true;
        }
        return false;
    }

    public List<Friendship> extractDataForRequestList(List<Friendship> list){
        List<Friendship> filter = new ArrayList<>();
        int req = 0;
        for(Friendship f: list){
            if((f.getUser1().getUsername().equals(user) || f.getUser2().getUsername().equals(user)) &&
                !f.getStatus().equals(FriendshipStatus.ACCEPTED)) {
                    if(!(f.getUser2().getUsername().equals(user) && f.getStatus().equals(FriendshipStatus.CANCELED)))
                        filter.add(f);
            }
            if(f.getUser2().getUsername().equals(user) && f.getStatus().equals(FriendshipStatus.PENDING)){
                ++req;
            }
        }
        numberOfRequests.setText("You have 0 new friend request");
        if(req != 0){
            numberOfRequests.setText("You have *" + Integer.toString(req) + "* new friend request");
        }
        return filter;
    }

    public void initModel() {
        Iterable<Friendship> friendships = serviceFriendship.findAllFriendships();
        List<Friendship> friendshipList = StreamSupport.stream(friendships.spliterator(), false).collect(Collectors.toList());
        List<String> friendsFormatted = extractFriendsForFriendList(friendshipList);
        numberOfFriends.setText("You have " + friendsFormatted.size() + " friends");

        friendsModel.setAll(friendsFormatted);
        myFriends.setItems(friendsModel);

        ObservableList<Friendship> friendshipObservableList = friendRequestsTable.getItems();
        friendshipObservableList.clear();
        friendshipObservableList.addAll(extractDataForRequestList(friendshipList));
        friendRequestsTable.setItems(friendshipObservableList);
    }

    public void init() {
        initModel();

        firstName.setText(userEntity.getFirstName());
        lastName.setText(userEntity.getLastName());
        username.setText(userEntity.getUsername());
        friendRequestButton.setDisable(true);
        chatButton.setDisable(true);
        removeFriendButton.setDisable(true);

        Credentials credentials = new Credentials();
        MessageValidator messageValidator = new MessageValidator();
        MessageDatabaseRepository repository = new MessageDatabaseRepository(credentials.getUrl(),
                credentials.getUsername(), credentials.getPassword(), messageValidator);
        this.serviceMessage = new ServiceMessage(repository);


        //Image imProfile = new Image(getClass().getResourceAsStream("/img/profile128.png"));

        long avatarNumber = userEntity.getId() % 13;

        Image userAvatar = new Image("/avatars/avatar" + avatarNumber + ".png");
        avatar.setImage(userAvatar);


        initSearchList();
        initFriendsList();
    }

    private void initSearchList(){
        usersList = StreamSupport.stream(serviceUser.findAllUsers().spliterator(), false).collect(Collectors.toList());
        searchListResults.setAll(extractDataForSearchResults(usersList));
        searchResults.setItems(searchListResults);
        searchResults.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setSelectedUser(newValue);
            }
        });
    }

    private void initFriendsList(){
        myFriends.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                setSelectedUser(newValue);
            }
        });
    }

    @FXML
    private void search(ActionEvent event){
        List<String> result = searchList(searchBar.getText(), usersForSearchList);
        searchResults.getItems().clear();
        searchResults.getItems().addAll(result);
    }

    private List<String> searchList(String searchWords, List<String> listOfStrings){
        List<String> searchWordsArray = Arrays.asList(searchWords.trim().split(" "));


        return listOfStrings.stream().filter(input -> {
            return searchWordsArray.stream().allMatch(word ->
                    input.toLowerCase().contains(word.toLowerCase()));
        }).collect(Collectors.toList());
    }

    @FXML
    private void onLogOutButtonClicked(ActionEvent event) throws IOException{
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Log out");

        alert.setContentText("Press OK if you want to log out");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isEmpty()){
            System.out.println("Logout canceled");
        }else if(result.get() == ButtonType.CANCEL){
            System.out.println("Logout canceled");
        }
        else if(result.get() == ButtonType.OK){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            root = loader.load();

            System.out.println("Login Button pressed");

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML
    private void onRemoveFriendButtonClicked() throws IOException {
        System.out.println(selectedUser.getUsername() + " deleted");
        removeFriendButton.setDisable(true);
        serviceFriendship.deleteFriendship(user, selectedUser.getUsername());
    }

    @FXML
    private void onSendFriendRequestButtonClicked(){
        System.out.println(selectedUser.getUsername() + " sent friend request");
        friendRequestButton.setDisable(true);
        try{
            serviceFriendship.addFriendship(user, selectedUser.getUsername(), FriendshipStatus.PENDING);
        }
        catch (Exception e){
            serviceFriendship.updateFriendship(user, selectedUser.getUsername(), FriendshipStatus.PENDING);
            System.out.println("Friend request already exists!");
        }
    }

    @FXML
    private void onDeleteFriendshipClicked(){
        if(selectedFriendship == null){
            System.out.println("Not ready");
            return;
        }
        try {
            serviceFriendship.deleteFriendship(user, selectedFriendship.getUser2().getUsername());
            serviceFriendship.deleteFriendship(selectedFriendship.getUser2().getUsername(), user);
        }
        catch (Exception e){
            System.out.println("Something wrong happened");
        }
    }

    @FXML
    private void onCancelRequestClicked(){
        if(selectedFriendship == null){
            System.out.println("Not ready");
            return;
        }
        try {
            serviceFriendship.updateFriendship(user, selectedFriendship.getUser2().getUsername(), FriendshipStatus.CANCELED);
        }
        catch (Exception e){
            System.out.println("Something wrong happened");
        }
    }

    @FXML
    private void onAcceptClicked(){
        if(selectedFriendship == null){
            System.out.println("Not ready");
            return;
        }
        try {
            serviceFriendship.updateFriendship(selectedFriendship.getUser1().getUsername(), user, FriendshipStatus.ACCEPTED);
        }
        catch (Exception e){
            System.out.println("Something wrong happened");
        }
    }

    @FXML
    private void onRejectClicked(){
        if(selectedFriendship == null){
            System.out.println("Not ready");
            return;
        }
        try {
            serviceFriendship.updateFriendship(selectedFriendship.getUser1().getUsername(), user, FriendshipStatus.REJECTED);
        }
        catch (Exception e){
            System.out.println("Something wrong happened");
        }

    }

    @FXML
    private void onChatClicked(){
        chatIdle.setText("");
        List<Message> conversation = serviceMessage.findConversation(user, selectedUser.getUsername());
        for(Message message : conversation){
            if(Objects.equals(message.getFromUser(), user)){
                chatMessages.getItems().add("From " + user + ": " + message.getText());
            }
            if(Objects.equals(message.getFromUser(), selectedUser.getUsername())){
                chatMessages.getItems().add("From " + selectedUser.getUsername() + ": " + message.getText());
            }
        }
    }

    @FXML
    private void onSendMessageClicked(){
        if(messageBar.getText().equals("") || selectedUser.getUsername().equals(user)){
            messageBar.clear();
            return;
        }
        serviceMessage.addMessage(user, selectedUser.getUsername() , messageBar.getText(), LocalDateTime.now());
        chatMessages.getItems().add("From " + user + ": " + messageBar.getText());
        messageBar.clear();
    }
}
