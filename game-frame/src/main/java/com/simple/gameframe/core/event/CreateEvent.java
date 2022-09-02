package com.simple.gameframe.core.event;

import com.simple.api.game.Room;

public class CreateEvent implements Event {

    private Room room;

    private Object o;

    public void setRoom(Room room){
        this.room = room;
    }

    public void setO(Object o){
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
