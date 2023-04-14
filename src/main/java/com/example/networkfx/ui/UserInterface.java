package com.example.networkfx.ui;
import com.example.networkfx.domain.Friendship;
import com.example.networkfx.domain.FriendshipStatus;
import com.example.networkfx.service.ServiceFriendship;
import com.example.networkfx.service.ServiceUser;
import com.example.networkfx.domain.validators.ValidationException;
import com.example.networkfx.domain.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInterface {
    private final ServiceUser serviceUser;
    private final ServiceFriendship serviceFriendship;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public UserInterface(ServiceUser serviceUser, ServiceFriendship serviceFriendship) {
        this.serviceUser = serviceUser;
        this.serviceFriendship = serviceFriendship;
    }

    public void Menu(){
        System.out.println("add_user - Add one user");
        System.out.println("all_users - See all users");
        System.out.println("delete_user - Delete one user");
        System.out.println("add_friends - Add one friendship");
        System.out.println("all_friends - See all friends");
        System.out.println("delete_friends - Delete one friends");
        System.out.println("number_com - Number of communities");
        System.out.println("biggest_com - Biggest community");
    }

    public void Start() {
        Menu();
        while(true) {
            try {
                String choice = reader.readLine();
                switch (choice){
                    case "add_user":
                        AddUser();
                        break;
                    case "all_users":
                        AllUser();
                        break;
                    case "delete_user":
                        DeleteUser();
                        break;
                    case "add_friends":
                        AddFriends();
                        break;
                    case "all_friends":
                        AllFriends();
                        break;
                    case "delete_friends":
                        DeleteFriends();
                        break;
                    case "number_com":
                        NumberOfCommunities();
                        break;
                    case "biggest_com":
                        BiggestCommunity();
                        break;
                    case "exit":
                        return;
                    case "help":
                        Menu();
                        break;
                    default:
                        System.out.println("Typo");
                }
            } catch (IOException | ValidationException e) {
                System.out.println(e.toString());
                //e.printStackTrace();
            }
        }
    }

    public void AddUser() throws IOException, ValidationException {
        System.out.println("First Name:");
        String firstName = reader.readLine();

        System.out.println("Last Name:");
        String lastName = reader.readLine();

        System.out.println("Username:");
        String username = reader.readLine();

        System.out.println("Email:");
        String email = reader.readLine();

        System.out.println("Password:");
        String password = reader.readLine();

        User user;
        user = serviceUser.addUser(firstName, lastName, username, email, password);
        if(user == null){
            System.out.println("User added successfully");
        }else {
            System.out.println("Something went wrong");
        }

    }
    public void AllUser() throws IOException {
        Iterable<User> UserList = serviceUser.findAllUsers();
        for(User user : UserList){
            System.out.println(user.toString());
        }

    }
    public void DeleteUser() throws IOException {
        System.out.println("Username:");
        String username = reader.readLine();
        User user;
        user = serviceUser.deleteUser(username);
        if(user == null){
            System.out.println("This user does not exist");
        }
        else{
            System.out.println("User deleted successfully");
        }
    }
    public void AddFriends() throws IOException {
        System.out.println("For first person");
        System.out.println("Username:");
        String username1 = reader.readLine();

        System.out.println("For second person");
        System.out.println("Username:");
        String username2 = reader.readLine();

        Friendship friendship = serviceFriendship.addFriendship(username1, username2, FriendshipStatus.ACCEPTED);
        if(friendship == null){
            System.out.println("Friendship added successfully");
        }
        else {
            System.out.println("Friendship cannot be created");
        }
    }
    public void AllFriends() throws IOException {
        Iterable<Friendship> list = serviceFriendship.findAllFriendships();
        for(Friendship friendship : list) {
            System.out.println(friendship.toString());
        }

    }
    public void DeleteFriends() throws IOException {
        System.out.println("For first person");
        System.out.println("Username:");
        String username1 = reader.readLine();

        System.out.println("For second person");
        System.out.println("Username:");
        String username2 = reader.readLine();

        Friendship friendship = serviceFriendship.deleteFriendship(username1, username2);
        if(friendship == null){
            System.out.println("Friendship does not exist");
        }
        else {
            System.out.println("Friendship deleted successfully");
        }
    }
    public void UpdateUser() throws IOException, ValidationException {
        System.out.println("Enter the username of the user you want to update:");
        String old_username = reader.readLine();

        System.out.println("First Name:");
        String firstName = reader.readLine();

        System.out.println("Last Name:");
        String lastName = reader.readLine();

        System.out.println("Username:");
        String username = reader.readLine();

        System.out.println("Email:");
        String email = reader.readLine();

        System.out.println("Password:");
        String password = reader.readLine();

        User user;
        user = serviceUser.addUser(firstName, lastName, username, email, password);
        if(user == null){
            System.out.println("User added successfully");
        }else {
            System.out.println("Something went wrong");
        }
    }

    public void NumberOfCommunities(){
        System.out.println("Number of communities:");
        //System.out.println(serviceUser.GraphComponents());
    }

    public void BiggestCommunity() {
        System.out.println("Biggest community:");
        //System.out.println(service.LongestPath());
    }
}
