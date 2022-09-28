package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class JoinEvent extends AbstractEvent {

    public JoinEvent(Room<Player> room,Player player, Object o) {
        super(room, player, o);
    }
}
