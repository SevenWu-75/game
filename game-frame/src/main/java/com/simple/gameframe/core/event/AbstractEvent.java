package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class AbstractEvent implements Event{

    private final Room<Player> room;

    private final Object o;

    private final Player player;

    public AbstractEvent(Room<Player> room, Player player, Object o){
        this.room = room;
        this.o = o;
        this.player = player;
    }

    @Override
    public Room<Player> getRoom() {
        return this.room;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Object getO() {
        return this.o;
    }
}
