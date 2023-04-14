package com.example.networkfx.service;

import com.example.networkfx.domain.Friendship;
import com.example.networkfx.repository.memory.InMemoryRepository0;
import com.example.networkfx.domain.User;
import com.example.networkfx.domain.Graph;
import com.example.networkfx.repository.database.FriendsDatabaseRepository;
import com.example.networkfx.repository.database.UserDatabaseRepository;
import com.example.networkfx.utils.events.ChangeEventType;
import com.example.networkfx.utils.events.FriendEntityChangeEvent;
import com.example.networkfx.utils.observer.Observable;
import com.example.networkfx.utils.observer.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements Observable<FriendEntityChangeEvent> {
    private final List<Observer<FriendEntityChangeEvent>> observers = new ArrayList<>();
    private final UserDatabaseRepository RepoUser;
    private final FriendsDatabaseRepository RepoFriendship;


    /**
     * @param username
     * @return -the id of the entity to be returned
     * -1 if the entity is not found
     */
    protected Long findUserID(String username) {
        Iterable<User> list = findAllUsers();
        for (User user : list) {
            if (user.getUsername().equals(username)) {
                return user.getId();
            }
        }
        return (long) -1;
    }

    /**
     * @param username1, username2
     * @return -the id of the entity to be returned
     * -1 if the entity is not found
     */
    protected Long findFriendshipId(String username1, String username2) {
        Iterable<Friendship> list = findAllFriendships();
        for (Friendship friendship : list) {
            if (friendship.getUser1().getUsername().equals(username1) &&
                    friendship.getUser2().getUsername().equals(username2)) {
                return friendship.getId();
            }
        }
        return (long) -1;
    }

    //Constructor
    public Service(UserDatabaseRepository repo1, FriendsDatabaseRepository repo2) {
        RepoUser = repo1;
        RepoFriendship = repo2;
    }

    //return all friendships
    public Iterable<Friendship> findAllFriendships() {
        return RepoFriendship.findAll();
    }

    //return all users
    public Iterable<User> findAllUsers() {
        return RepoUser.findAll();
    }

    /*
     *param : Long id
     * return the friendship with the specified id
     */
    public Friendship findFriendship(Long id) {
        return RepoFriendship.findOne(id);
    }

    /**
     * @param username
     * @return -the entity to be returned
     * null if the entity is not found
     */
    public User findUser(String username) {
        Long id = findUserID(username);
        if (id == -1)
            return null;

        return RepoUser.findOne(id);
    }

    /**
     * @param username1, username2
     * @return -RepoFriendship.delete(entity)
     * the created friendship
     */
    public Friendship deleteFriendship(String username1, String username2) throws IOException {
        Long id = findFriendshipId(username1, username2);
        if (id == -1) {
            id = findFriendshipId(username2, username1);
        }
        Friendship returnedFriendship = RepoFriendship.delete(id);
        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.DELETE, findFriendship(id)));
        return returnedFriendship;
    }

    public void deleteFriendshipsWith(User user) throws IOException{
        Iterable<Friendship> friendships = findAllFriendships();
        List<String> friends = new ArrayList<>();

        for (Friendship friendship : friendships) {
            if (friendship.getUser1().equals(user)) {
                friends.add(friendship.getUser2().getUsername());
            }
            if(friendship.getUser2().equals(user)){
                friends.add(friendship.getUser1().getUsername());
            }
        }

        for(String friend : friends){
            deleteFriendship(user.getUsername(), friend);
        }
    }

    @Override
    public void addObserver(Observer<FriendEntityChangeEvent> e) { observers.add(e); }

    @Override
    public void removeObserver(Observer<FriendEntityChangeEvent> e) { observers.remove(e); }

    @Override
    public void notifyObservers(FriendEntityChangeEvent t){
        observers.stream().forEach(x -> x.update(t));
    }

}
