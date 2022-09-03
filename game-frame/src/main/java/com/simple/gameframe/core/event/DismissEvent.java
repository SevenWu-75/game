package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class DismissEvent extends AbstractEvent {

    public DismissEvent(Room room, Object o) {
        super(room, o);
    }
}
