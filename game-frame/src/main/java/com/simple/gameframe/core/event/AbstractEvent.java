package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class AbstractEvent implements Event{

    private Room room;

    private Object o;

    public AbstractEvent(Room room, Object o){
        this.room = room;
        this.o = o;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

    @Override
    public Object getO() {
        return this.o;
    }
}
