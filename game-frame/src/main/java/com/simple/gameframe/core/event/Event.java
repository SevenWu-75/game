package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public interface Event {

    Room<? extends Player> getRoom();

    Object getO();
}
