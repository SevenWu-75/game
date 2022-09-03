package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class DisconnectEvent extends AbstractEvent {

    public DisconnectEvent(Room room, Object o) {
        super(room, o);
    }
}
