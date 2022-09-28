package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class SeatDownEvent extends AbstractEvent {

    public SeatDownEvent(Room<Player> room,Player player, Object o) {
        super(room, player, o);
    }
}
