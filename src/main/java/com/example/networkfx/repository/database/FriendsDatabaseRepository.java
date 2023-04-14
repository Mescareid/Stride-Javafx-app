package com.example.networkfx.repository.database;

import com.example.networkfx.domain.FriendshipStatus;
import com.example.networkfx.domain.User;
import com.example.networkfx.domain.Friendship;
import com.example.networkfx.domain.validators.ValidationException;
import com.example.networkfx.domain.validators.Validator;
import com.example.networkfx.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class FriendsDatabaseRepository implements Repository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Friendship> validator;

    public FriendsDatabaseRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship findOne(Long aLong) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement
                     ("SELECT * FROM friendship WHERE id = " + aLong.toString());
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                String user1 = resultSet.getString("user1");
                String user2 = resultSet.getString("user2");
                LocalDateTime friendsFrom = resultSet.getObject("friendsfrom", java.time.LocalDateTime.class);
                String status = resultSet.getString("status");

                Iterable<User> users = findAllUsers();

                User firstUser = new User("", "", "", "", "");
                User secondUser = new User("", "", "", "", "");

                for (User user : users) {
                    if (user.getUsername().equals(user1)) {
                        firstUser.setFirstName(user.getFirstName());
                        firstUser.setLastName(user.getLastName());
                        firstUser.setUsername(user.getUsername());
                        firstUser.setEmail(user.getUsername());
                        firstUser.setPassword(user.getPassword());
                        firstUser.setId(user.getId());
                    }
                    if (user.getUsername().equals(user2)) {
                        secondUser.setFirstName(user.getFirstName());
                        secondUser.setLastName(user.getLastName());
                        secondUser.setUsername(user.getUsername());
                        secondUser.setEmail(user.getUsername());
                        secondUser.setPassword(user.getPassword());
                        secondUser.setId(user.getId());
                    }
                }

                FriendshipStatus friendshipStatus = switch (status) {
                    case "ACCEPTED" -> FriendshipStatus.ACCEPTED;
                    case "PENDING" -> FriendshipStatus.PENDING;
                    case "CANCELED" -> FriendshipStatus.CANCELED;
                    case "REJECTED" -> FriendshipStatus.REJECTED;
                    default -> throw new ValidationException("Friendship Status Error");
                };

                Friendship friendship = new Friendship(firstUser, secondUser, friendsFrom, friendshipStatus);
                friendship.setId(aLong);
                return friendship;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendship");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String user1 = resultSet.getString("user1");
                String user2 = resultSet.getString("user2");
                LocalDateTime friendsFrom = resultSet.getObject("friendsfrom", java.time.LocalDateTime.class);
                String status = resultSet.getString("status");

                Iterable<User> users = findAllUsers();

                User firstUser = new User("", "", "", "", "");
                User secondUser = new User("", "", "", "", "");

                for (User user : users) {
                    if (user.getUsername().equals(user1)) {
                        firstUser.setFirstName(user.getFirstName());
                        firstUser.setLastName(user.getLastName());
                        firstUser.setUsername(user.getUsername());
                        firstUser.setEmail(user.getUsername());
                        firstUser.setPassword(user.getPassword());
                        firstUser.setId(user.getId());
                    }
                    if (user.getUsername().equals(user2)) {
                        secondUser.setFirstName(user.getFirstName());
                        secondUser.setLastName(user.getLastName());
                        secondUser.setUsername(user.getUsername());
                        secondUser.setEmail(user.getUsername());
                        secondUser.setPassword(user.getPassword());
                        secondUser.setId(user.getId());
                    }
                }

                FriendshipStatus friendshipStatus = switch (status) {
                    case "ACCEPTED" -> FriendshipStatus.ACCEPTED;
                    case "PENDING" -> FriendshipStatus.PENDING;
                    case "CANCELED" -> FriendshipStatus.CANCELED;
                    case "REJECTED" -> FriendshipStatus.REJECTED;
                    default -> throw new ValidationException("Friendship Status Error");
                };

                Friendship friendship = new Friendship(firstUser, secondUser, friendsFrom, friendshipStatus);
                friendship.setId(id);
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    public Iterable<User>   findAllUsers() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(firstName, lastName, username, email, password);
                user.setId(id);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Friendship save(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);

        String sql = "INSERT INTO friendship (user1, user2, friendsfrom, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            statement.setString(1, entity.getUser1().getUsername());
            statement.setString(2, entity.getUser2().getUsername());
            statement.setObject(3, entity.getFriendsFrom());

            String status = switch (entity.getStatus()) {
                case ACCEPTED -> "ACCEPTED";
                case REJECTED -> "REJECTED";
                case PENDING -> "PENDING";
                case CANCELED -> "CANCELED";
            };

            statement.setString(4, status);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long aLong) {
        Friendship friendship = findOne(aLong);
        if (friendship == null) {
            return null;
        }
        String sql = "DELETE FROM friendship WHERE id = " + aLong.toString();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendship;
    }

    @Override
    public Friendship update(Friendship entity) {
        Long id = entity.getId();
        Friendship oldFriendship = findOne(id);
        if (oldFriendship == null) {
            return null;
        }
        String sql = "UPDATE friendship SET user1 = ?, user2 = ?, friendsfrom = ?, status = ? WHERE id = " + id.toString();

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            statement.setString(1, entity.getUser1().getUsername());
            statement.setString(2, entity.getUser2().getUsername());
            statement.setObject(3, entity.getFriendsFrom());

            String status = switch (entity.getStatus()) {
                case ACCEPTED -> "ACCEPTED";
                case REJECTED -> "REJECTED";
                case PENDING -> "PENDING";
                case CANCELED -> "CANCELED";
            };

            statement.setString(4, status);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oldFriendship;
    }

}
