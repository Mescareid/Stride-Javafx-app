package com.example.networkfx.repository.file;

import com.example.networkfx.domain.Friendship;
import com.example.networkfx.domain.FriendshipStatus;
import com.example.networkfx.domain.User;
import com.example.networkfx.domain.validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendsFile0 extends AbstractFileRepository0<Long, Friendship> {
    public FriendsFile0(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        User user1 = new User(attributes.get(1));
        User user2 = new User(attributes.get(2));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime friendsFrom = LocalDateTime.parse(attributes.get(3), formatter);
        Friendship friendship = new Friendship(user1, user2, friendsFrom, FriendshipStatus.ACCEPTED);
        friendship.setId(Long.parseLong(attributes.get(0)));

        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return entity.getId() + ";" + entity.getUser1().getUsername() + ";" + entity.getUser2().getUsername() + ";" +
                entity.getFriendsFrom().format(format);
    }
}
