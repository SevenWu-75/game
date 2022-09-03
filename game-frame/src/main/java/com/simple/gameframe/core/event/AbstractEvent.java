package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class AbstractEvent implements Event{

    private final Room<? extends Player> room;

    private final Object o;

    public AbstractEvent(Room<? extends Player> room, Object o){
        this.room = room;
        this.o = o;
    }

    @Override
    public Room<? extends Player> getRoom() {
        return this.room;
    }

    @Override
    public Object getO() {
        return this.o;
    }
}
