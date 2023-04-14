package com.example.networkfx.repository.file;

import com.example.networkfx.domain.User;
import com.example.networkfx.domain.validators.Validator;

import java.util.List;

public class UtilizatorFile0 extends AbstractFileRepository0<Long, User> {

    public UtilizatorFile0(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    @Override
    public User extractEntity(List<String> attributes) {
        //TODO: implement method
        User user = new User(attributes.get(1), attributes.get(2), attributes.get(3),
                attributes.get(4), attributes.get(5));
        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName() + ";" + entity.getUsername()
                + ";" + entity.getEmail() + ";" + entity.getPassword();
    }
}
