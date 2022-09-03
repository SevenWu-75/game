package com.simple.gameframe.core.event;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

public interface Event {

    Room getRoom();

    Object getO();
}
