package com.example.networkfx.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import com.example.networkfx.domain.FriendshipStatus;

public class Friendship extends Entity<Long> {
    private User user1;
    private User user2;
    private LocalDateTime friendsFrom;
    private FriendshipStatus status;

    public Friendship(User user1, User user2, LocalDateTime friendsFrom, FriendshipStatus friendshipStatus) {
        this.user1 = user1;
        this.user2 = user2;
        this.friendsFrom = friendsFrom;
        this.status = friendshipStatus;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public FriendshipStatus getStatus() { return status; }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public void setFriendsFrom(LocalDateTime when) {
        this.friendsFrom = when;
    }

    public void setStatus(FriendshipStatus friendshipStatus) { this.status = friendshipStatus; }

    @Override
    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return "Friendship{" +
                "user1='" + user1.getUsername() + '\'' +
                ", user2='" + user2.getUsername() + '\'' +
                ", friends from='" + this.getFriendsFrom().format(format) + '\'' +
                ", friends stats='" + this.getStatus().toString() + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return getUser1().equals(that.getUser1()) && getUser2().equals(that.getUser2()) &&
                getFriendsFrom().equals(that.getFriendsFrom()) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser1(), getUser2(), getFriendsFrom(), status);
    }
}
