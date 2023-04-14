package com.example.networkfx.utils.observer;

import com.example.networkfx.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
