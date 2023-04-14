package com.example.networkfx;

import com.example.networkfx.config.Credentials;
import com.example.networkfx.domain.validators.FriendshipValidator;
import com.example.networkfx.domain.validators.UserValidator;
import com.example.networkfx.repository.database.FriendsDatabaseRepository;
import com.example.networkfx.repository.database.UserDatabaseRepository;
import com.example.networkfx.service.ServiceFriendship;
import com.example.networkfx.service.ServiceUser;
import com.example.networkfx.AppController;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class LoginController{
    private Stage stage;
    private Scene scene;
    private Parent root;
    private ServiceUser serviceUser;
    private ServiceFriendship serviceFriendship;
    @FXML
    private Pane pane1;
    @FXML
    private Pane pane2;
    @FXML
    private Label loginLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label specialLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label errorLogInLabel;
    @FXML
    private Label errorSignUpLabel;
    @FXML
    private Label jokeLabel;
    @FXML
    private CheckBox rememberCheck;
    @FXML
    private Hyperlink signUp;
    @FXML
    private Hyperlink logIn;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameFieldSignUp;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordFieldSignUp;
    @FXML
    private PasswordField retypePasswordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button SignUpButton;
    @FXML
    private ImageView curtain1;
    @FXML
    private ImageView curtain2;


    private ArrayList<Node> loginView = new ArrayList<>();
    private ArrayList<Node> signupView = new ArrayList<>();

    public LoginController(){
        this.serviceUser = null;
        this.serviceFriendship = null;
    }

    private void loadLoginView(){
        loginView.add(loginLabel);
        loginView.add(usernameLabel);
        loginView.add(usernameField);
        loginView.add(passwordLabel);
        loginView.add(passwordField);
        loginView.add(rememberCheck);
        loginView.add(specialLabel);
        loginView.add(loginButton);
        loginView.add(accountLabel);
        loginView.add(signUp);
    }

    @FXML
    public void initialize() throws FileNotFoundException {
        loadLoginView();
        curtain1.setDisable(true);
        curtain1.setOpacity(0);
        curtain2.setDisable(false);
        curtain2.setOpacity(1.0);
        FileInputStream inputstream = new FileInputStream("C:\\Users\\Mescareid\\OneDrive\\Desktop\\Facultate\\MAP\\Network\\NetworkFX\\src\\main\\resources\\com\\example\\networkfx\\icon3.png");
        BackgroundImage myBI= new BackgroundImage(
                new Image(inputstream,100,100,true,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        //then you set to your node
        pane2.setBackground(new Background(myBI));

        Credentials config = new Credentials();
        UserValidator userValidator = new UserValidator();
        FriendshipValidator friendshipValidator = new FriendshipValidator();
        UserDatabaseRepository RepoUser = new UserDatabaseRepository(config.getUrl(),
                config.getUsername(), config.getPassword(), userValidator);
        FriendsDatabaseRepository RepoFriends = new FriendsDatabaseRepository(config.getUrl(),
                config.getUsername(), config.getPassword(), friendshipValidator);
        this.serviceUser = new ServiceUser(RepoUser, RepoFriends);
        this.serviceFriendship = new ServiceFriendship(RepoUser, RepoFriends);
    }
    @FXML
    protected void onLogInClick(ActionEvent event){
        System.out.println("Log In Button was pressed");
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(username.equals("") || password.equals("")){
            errorLogInLabel.setText("Username or password cannot be empty");
            usernameField.clear();
            passwordField.clear();
            return;
        }
        System.out.println(username);
        System.out.println(password);
        int hash = password.hashCode();
        String result = serviceUser.logInUser(username, Integer.toString(hash));
        if(result.equals("OK!")){
            try{
                switchToApp(event, username);
            }
            catch (Exception e){
                System.out.println(e);
            }
        }
        errorLogInLabel.setText(result);
        System.out.println(result);
        usernameField.clear();
        passwordField.clear();
    }

    protected void switchToApp(ActionEvent event, String username) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("app.fxml"));
        root = loader.load();

        AppController appController = loader.getController();
        appController.setServices(serviceUser, serviceFriendship);
        appController.setUser(username);
        appController.init();

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onSignUpButton(ActionEvent event){
        System.out.println("Sign up Button was pressed");
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String username = usernameFieldSignUp.getText();
        String email = emailField.getText();
        String password = passwordFieldSignUp.getText();
        String retypedPassword = retypePasswordField.getText();
        ArrayList<String> inputs = new ArrayList<>();
        inputs.add(firstName); inputs.add(lastName); inputs.add(username); inputs.add(email); inputs.add(password);
        inputs.add(retypedPassword);
        for(String input : inputs){
            if(input.equals("")){
                errorSignUpLabel.setText("Field(s) cannot be empty!");
                return;
            }
        }
        if(!password.equals(retypedPassword)){
            errorSignUpLabel.setText("Passwords doesn't match");
            passwordFieldSignUp.clear();
            retypePasswordField.clear();
            return;

        }

        try{
            int hash = password.hashCode();
            serviceUser.addUser(firstName, lastName, username, email, Integer.toString(hash));
            switchToApp(event, username);
        }
        catch (Exception e){
            System.out.println(e);
            errorSignUpLabel.setText(e.getMessage());
            firstNameField.clear();
            lastNameField.clear();
            usernameFieldSignUp.clear();
            emailField.clear();
            passwordFieldSignUp.clear();
            retypePasswordField.clear();
        }
    }
    @FXML
    protected void onCheckClick(){
        if(rememberCheck.isSelected()) {
            System.out.println("Check was pressed");
            specialLabel.setText("You are not that special.");
            serviceFriendship.generateGraph();
        }
        else {
            specialLabel.setText("");
        }
    }
    @FXML
    protected void onTermsClick(){
        jokeLabel.setText("For real?");
    }

    @FXML
    protected void switchToSignUp(){
        System.out.println("Switched to Sign Up");
        curtain2.setDisable(true);
        FadeTransition transition = new FadeTransition(Duration.seconds(1),  curtain2);
        transition.setFromValue(1.0);
        transition.setToValue(0);
        transition.play();

        curtain1.setDisable(false);
        FadeTransition transition2 = new FadeTransition(Duration.seconds(1),  curtain1);
        transition2.setFromValue(0);
        transition2.setToValue(1.0);
        transition2.play();
    }
    @FXML
    protected void switchToLogIn(){
        System.out.println("Switched to log in");
        curtain2.setDisable(false);
        FadeTransition transition = new FadeTransition(Duration.seconds(1),  curtain2);
        transition.setFromValue(0);
        transition.setToValue(1.0);
        transition.play();

        curtain1.setDisable(true);
        FadeTransition transition2 = new FadeTransition(Duration.seconds(1),  curtain1);
        transition2.setFromValue(1.0);
        transition2.setToValue(0);
        transition2.play();
    }
    protected void fadeOutLogin(){
        System.out.println("Fading out");
        ArrayList<FadeTransition> transitions = new ArrayList<>();
        for(Node o : loginView) {
            FadeTransition transition = new FadeTransition(Duration.seconds(1),  o);
            transition.setFromValue(1.0);
            transition.setToValue(0);
            transitions.add(transition);
        }
        for(FadeTransition f : transitions){
            f.play();
        }

    }
}
