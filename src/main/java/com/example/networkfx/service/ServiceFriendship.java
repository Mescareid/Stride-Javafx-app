package com.example.networkfx.service;

import com.example.networkfx.domain.Friendship;
import com.example.networkfx.repository.memory.InMemoryRepository0;
import com.example.networkfx.domain.User;
import com.example.networkfx.repository.database.FriendsDatabaseRepository;
import com.example.networkfx.repository.database.UserDatabaseRepository;
import com.example.networkfx.domain.FriendshipStatus;
import com.example.networkfx.utils.events.ChangeEventType;
import com.example.networkfx.utils.events.FriendEntityChangeEvent;
import com.example.networkfx.utils.observer.Observable;
import com.example.networkfx.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;


public class ServiceFriendship extends Service {
    private final UserDatabaseRepository RepoUser;
    private final FriendsDatabaseRepository RepoFriendship;
    private Long ID_Friendships;

    private Long lastID(){
        Iterable<Friendship> friends = findAllFriendships();
        Long last = 0L;
        for(Friendship friend : friends){
            if(friend.getId() > last){
                last = friend.getId();
            }
        }

        return last;
    }

    public ServiceFriendship(UserDatabaseRepository repo1, FriendsDatabaseRepository repo2) {
        super(repo1, repo2);
        RepoUser = repo1;
        RepoFriendship = repo2;
        ID_Friendships = lastID();
    }


    //getter for the id of friendships
    public Long getID_Friendships() {
        ID_Friendships++;
        return ID_Friendships;
    }

    /**
     *
     * @param username1, username2
     * @return -RepoFriendship.save(entity)
     *          the created friendship
     */
    public Friendship addFriendship(String username1, String username2, FriendshipStatus status) {
        User user1 = findUser(username1);
        User user2 = findUser(username2);
        Friendship friendship = new Friendship(user1, user2, LocalDateTime.now(), status);
        if(user1 == null || user2 == null){
            return friendship;
        }

        Iterable<Friendship> friendships = findAllFriendships();
        for(Friendship friendship1 : friendships){
            if((friendship1.getUser1().getUsername().equals(username1) &&
                    friendship1.getUser2().getUsername().equals(username2))  ||
                    (friendship1.getUser2().getUsername().equals(username1) &&
                            friendship1.getUser1().getUsername().equals(username2))){
                return friendship;
            }
        }

        Long currentID = getID_Friendships();
        friendship.setId(currentID);
        Friendship returnedFriendship = RepoFriendship.save(friendship);
        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.ADD, friendship));
        return returnedFriendship;
    }

    public Friendship updateFriendship(String username1, String username2, FriendshipStatus status){
        User user1 = findUser(username1);
        User user2 = findUser(username2);
        Friendship friendship = new Friendship(user1, user2, LocalDateTime.now(), status);
        if(user1 == null || user2 == null){
            return friendship;
        }

        Long id = -1L;

        Iterable<Friendship> friendships = findAllFriendships();
        for(Friendship friendship1 : friendships){
            if((friendship1.getUser1().getUsername().equals(username1) &&
                    friendship1.getUser2().getUsername().equals(username2))  ||
                    (friendship1.getUser2().getUsername().equals(username1) &&
                            friendship1.getUser1().getUsername().equals(username2))){
                id = friendship1.getId();
            }
        }

        if(id == -1){
            return friendship;
        }
        friendship.setId(id);

        Friendship returnedFriendship = RepoFriendship.update(friendship);

        notifyObservers(new FriendEntityChangeEvent(ChangeEventType.UPDATE, friendship));
        return returnedFriendship;
    }

    public boolean areFriends(User user1, User user2){
        Iterable<Friendship> friendships = findAllFriendships();

        for(Friendship friendship1 : friendships) {
            if ((friendship1.getUser1().getUsername().equals(user1.getUsername()) &&
                    friendship1.getUser2().getUsername().equals(user2.getUsername())) ||
                    (friendship1.getUser2().getUsername().equals(user2.getUsername()) &&
                            friendship1.getUser1().getUsername().equals(user1.getUsername())))
                return true;
        }

        return false;

    }

    public void generateGraph(){
        List<User> users = StreamSupport.stream(findAllUsers().spliterator(), false).toList();
        System.out.println(users.size());
        int[][] dp = new int[100][100];

        for(Friendship friendship : findAllFriendships()){
            if(friendship.getStatus() == FriendshipStatus.ACCEPTED){
                dp[Math.toIntExact(friendship.getUser1().getId()) - 16][Math.toIntExact(friendship.getUser2().getId()) - 16] = 1;
                dp[Math.toIntExact(friendship.getUser2().getId()) - 16][Math.toIntExact(friendship.getUser1().getId()) - 16] = 1;
            }
        }

        for(int i = 0; i < users.size(); ++i){
            for(int j = 0; j < users.size(); ++j){
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
    }



    //return all friendships
    public Iterable<Friendship> findAllFriendships() {
        return RepoFriendship.findAll();
    }

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

    public List<User> findAllFriendsOfUser(String username){
        List<User> friends = new ArrayList<>();
        Iterable<Friendship> iterable = findAllFriendships();
        for(Friendship friendship : iterable){
            if(friendship.getUser1().getUsername().equals(username)){
                friends.add(friendship.getUser2());
            }
            if(friendship.getUser2().getUsername().equals(username)){
                friends.add(friendship.getUser1());
            }
        }
        return friends;
    }

}
