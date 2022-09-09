package com.simple.gameframe.core.publisher;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.event.Event;
import com.simple.gameframe.core.listener.EventListener;

public interface EventPublisher {

    void create(Room<? extends Player> room, Player player, Object o);

    void join(Room<? extends Player> room, Player player, Object o);

    void canStart(Room<? extends Player> room, Player player, Object o);

    void start(Room<? extends Player> room, Player player, Object o);

    void turnNext(Room<? extends Player> room, Player player, Object o);

    void turnRound(Room<? extends Player> room, Player player, Object o);

    void voteDismiss(Room<? extends Player> room, Player player, Object o);

    void dismiss(Room<? extends Player> room, Player player, Object o);

    void gameOver(Room<? extends Player> room, Player player, Object o);

    void disconnect(Room<? extends Player> room, Player player, Object o);

    void reconnect(Room<? extends Player> room, Player player, Object o);

    void timeout(Room<? extends Player> room, Player player, Object o);

    void gameResult(Room<? extends Player> room, Player player, Object o);

    void seatDown(Room<? extends Player> room, Player player, Object o);

    void addListener(EventListener<?>... listeners);
}
