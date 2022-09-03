package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class ReconnectEvent extends AbstractEvent {

    public ReconnectEvent(Room room, Object o) {
        super(room, o);
    }
}
