package com.example.networkfx.domain.validators;

import com.example.networkfx.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    public FriendshipValidator() {
    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        String errMsg = "";
        if (entity.getId() == null || entity.getId() <= 0) {
            errMsg += "Id error at friendship ";
        }
        if (entity.getUser1() == null || entity.getUser2() == null) {
            errMsg += "unidirectional friendship ";
        }
        if (entity.getUser1().equals(entity.getUser2())) {
            errMsg += "User cannot be friend with himself";
        }
        if (!errMsg.equals("")) {
            throw new ValidationException(errMsg);
        }
    }
}
