package com.simple.gameframe.core.publisher;

import com.simple.api.game.Room;
import com.simple.gameframe.core.listener.EventListener;

public interface EventPublisher {

    void create(Room room, Object o);

    void join(Room room, Object o);

    void canStart(Room room, Object o);

    void start(Room room, Object o);

    void turnNext(Room room, Object o);

    void turnRound(Room room, Object o);

    void voteDismiss(Room room, Object o);

    void dismiss(Room room, Object o);

    void gameOver(Room room, Object o);

    void disconnect(Room room, Object o);

    void reconnect(Room room, Object o);

    void timeout(Room room, Object o);

    void gameResult(Room room, Object o);

    void addListener(EventListener<?>... listeners);
}
