package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class GameResultEvent extends AbstractEvent {

    public GameResultEvent(Room room, Object o) {
        super(room, o);
    }
}
