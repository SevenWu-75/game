package com.simple.gameframe.core.publisher;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.event.Event;
import com.simple.gameframe.core.listener.EventListener;

public interface EventPublisher {

    void create(Room<Player> room, Player player, Object o);

    void join(Room<Player> room, Player player, Object o);

    void canStart(Room<Player> room, Player player, Object o);

    void start(Room<Player> room, Player player, Object o);

    void turnNext(Room<Player> room, Player player, Object o);

    void turnRound(Room<Player> room, Player player, Object o);

    void voteDismiss(Room<Player> room, Player player, Object o);

    void dismiss(Room<Player> room, Player player, Object o);

    void gameOver(Room<Player> room, Player player, Object o);

    void disconnect(Room<Player> room, Player player, Object o);

    void reconnect(Room<Player> room, Player player, Object o);

    void timeout(Room<Player> room, Player player, Object o);

    void gameResult(Room<Player> room, Player player, Object o);

    void seatDown(Room<Player> room, Player player, Object o);

    void addListener(EventListener<?>... listeners);
}
