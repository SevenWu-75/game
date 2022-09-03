package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class GameOverEvent extends AbstractEvent {

    public GameOverEvent(Room room, Object o) {
        super(room, o);
    }
}
