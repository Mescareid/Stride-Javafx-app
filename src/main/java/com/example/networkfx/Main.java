package com.example.networkfx;


import com.example.networkfx.config.Credentials;
import com.example.networkfx.domain.FriendshipStatus;
import com.example.networkfx.domain.validators.FriendshipValidator;
import com.example.networkfx.domain.validators.UserValidator;
import com.example.networkfx.service.Service;
import com.example.networkfx.service.ServiceFriendship;
import com.example.networkfx.service.ServiceUser;
import com.example.networkfx.ui.UserInterface;
import com.example.networkfx.repository.database.UserDatabaseRepository;
import com.example.networkfx.repository.database.FriendsDatabaseRepository;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1100, 600);
            stage.setTitle("Stride App");
            stage.setScene(scene);
            stage.setResizable(false);
            FileInputStream inputstream = new FileInputStream("C:\\Users\\Mescareid\\OneDrive\\Desktop\\Facultate\\MAP\\Network\\NetworkFX\\src\\main\\resources\\com\\example\\networkfx\\icon_nobk.png");
            stage.getIcons().add(new Image(inputstream));
            stage.show();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}