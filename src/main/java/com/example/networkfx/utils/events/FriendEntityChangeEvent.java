package com.example.networkfx.utils.events;

import com.example.networkfx.domain.Friendship;


public class FriendEntityChangeEvent implements Event{
    private final ChangeEventType type;
    private final Friendship data;
    private Friendship oldData;

    public FriendEntityChangeEvent(ChangeEventType type, Friendship data){
        this.type = type;
        this.data = data;
    }

    public FriendEntityChangeEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData = oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }
}

