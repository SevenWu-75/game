package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class TurnNextEvent extends AbstractEvent {

    public TurnNextEvent(Room room, Object o) {
        super(room, o);
    }
}
