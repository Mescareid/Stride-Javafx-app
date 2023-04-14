package com.example.networkfx.domain.validators;

import com.example.networkfx.domain.Friendship;
import com.example.networkfx.domain.Message;

import java.util.Objects;

public class MessageValidator implements Validator<Message> {
    public MessageValidator() {
    }

    @Override
    public void validate(Message entity) throws ValidationException {
        String errMsg = "";
        if (entity.getToUser() == null || entity.getFromUser() == null) {
            errMsg += "user missing ";
        }
        if (entity.getToUser().equals(entity.getFromUser())) {
            errMsg += "User cannot send a message to himself";
        }
        if(Objects.equals(entity.getText(), "")){
            errMsg += "Message cannot be empty";
        }
        if (!errMsg.equals("")) {
            throw new ValidationException(errMsg);
        }
    }
}
