package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class TimeOutEvent extends AbstractEvent{

    public TimeOutEvent(Room room, Object o) {
        super(room, o);
    }
}
