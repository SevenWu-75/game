package com.simple.gameframe.core;

import com.simple.api.game.Player;

public interface RoundHandler {

    Player getNextPlayer();

    void handle();
}
