package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class JoinEvent extends AbstractEvent {

    public JoinEvent(Room room, Object o) {
        super(room, o);
    }
}
