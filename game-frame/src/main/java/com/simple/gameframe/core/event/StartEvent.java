package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class StartEvent extends AbstractEvent {

    public StartEvent(Room room, Object o) {
        super(room, o);
    }
}
