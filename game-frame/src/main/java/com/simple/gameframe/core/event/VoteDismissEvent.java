package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public class VoteDismissEvent extends AbstractEvent {

    public VoteDismissEvent(Room<? extends Player> room,Player player, Object o) {
        super(room, player, o);
    }
}
