package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class TimeOutEvent extends AbstractEvent{

    public TimeOutEvent(Room<? extends Player> room, Object o) {
        super(room, o);
    }
}
