package com.example.networkfx.service;

import com.example.networkfx.domain.Friendship;
import com.example.networkfx.repository.memory.InMemoryRepository0;
import com.example.networkfx.domain.User;
import com.example.networkfx.domain.validators.ValidationException;
import com.example.networkfx.domain.Graph;
import com.example.networkfx.repository.database.FriendsDatabaseRepository;
import com.example.networkfx.repository.database.UserDatabaseRepository;

import java.io.IOException;

public class ServiceUser extends Service {
    private final UserDatabaseRepository RepoUser;
    private Long ID_Users;

    private Long lastID(){
        Iterable<User> users = findAllUsers();
        Long last = 0L;
        for(User user : users){
            if(user.getId() > last){
                last = user.getId();
            }
        }

        return last;
    }

    public ServiceUser(UserDatabaseRepository repo1, FriendsDatabaseRepository repo2) {
        super(repo1, repo2);
        ID_Users = lastID();
        RepoUser = repo1;
    }

    //getter for the id of users
    public Long getID_Users() {
        ID_Users++;
        return ID_Users;
    }


    /**
     * @param FirstName, String, username, email, password
     *                   creates the new entities for the parameters
     * @return RepoUser.save(entity)
     */
    public User addUser(String FirstName, String LastName, String username, String email,
                        String password) throws ValidationException {
        Iterable<User> users = findAllUsers();
        for(User user : users){
            if(user.getUsername().equals(username)){
                throw new ValidationException("Username is already taken.");
            }
        }
        Long currentID = getID_Users();
        User user = new User(FirstName, LastName, username, email, password);
        user.setId(currentID);
        return RepoUser.save(user);
    }

    /**
     * @param username finds the id of that entity
     * @return RepoUser.delete(entity)
     * null if the id doesn't exist
     */
    public User deleteUser(String username) throws IOException {
        User user = findUser(username);
        Long id = findUserID(username);
        if (id == -1)
            return null;
        deleteFriendshipsWith(user);
        return RepoUser.delete(id);
    }

    public String logInUser(String username, String password){
        Iterable<User> users = findAllUsers();
        for(User user : users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)){
                return "OK!";
            }
        }
        return "Username or password is incorrect!";
    }

}
