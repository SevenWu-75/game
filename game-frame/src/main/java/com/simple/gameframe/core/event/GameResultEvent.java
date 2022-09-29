package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class GameResultEvent extends AbstractEvent {

    public GameResultEvent(Room<? extends Player> room,Player player, Object o) {
        super(room, player, o);
    }
}
