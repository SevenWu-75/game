package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class CreateEvent extends AbstractEvent {

    public CreateEvent(Room<Player> room,Player player, Object o) {
        super(room, player, o);
    }
}
