package com.example.networkfx.domain.validators;

import com.example.networkfx.domain.User;

public class UserValidator implements Validator<User> {
    public UserValidator() {
    }

    @Override
    public void validate(User entity) throws ValidationException {
        String errMsg = "";
        if (entity.getId() == null) {
            errMsg += "Id error ";
        }
        if (entity.getFirstName() == null || "".equals(entity.getFirstName())) {
            errMsg += "First name error ";
        }
        if (entity.getLastName() == null || "".equals(entity.getLastName())) {
            errMsg += "Last name error ";
        }
        if(entity.getUsername() == null || "".equals(entity.getUsername())){
            errMsg += "Username error ";
        }
        if(!entity.getEmail().contains("@")|| !entity.getEmail().contains(".") || entity.getEmail().length() < 5){
            errMsg += "*Email is invalid ";
        }
        if(entity.getPassword() == null || "".equals(entity.getPassword())){
            errMsg += "*Password error ";
        }
        if(entity.getPassword().length() < 8) {
            errMsg += "*Password must be at least 8 characters";
        }
        if (!errMsg.equals("")) {
            throw new ValidationException(errMsg);
        }
    }
}
