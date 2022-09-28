package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public interface Event {

    Room<Player> getRoom();

    Player getPlayer();

    Object getO();
}
