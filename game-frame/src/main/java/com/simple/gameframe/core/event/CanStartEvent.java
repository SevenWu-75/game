package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class CanStartEvent extends AbstractEvent {

    public CanStartEvent(Room room, Object o) {
        super(room, o);
    }
}
