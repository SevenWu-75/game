package com.simple.gameframe.core.publisher;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.event.*;
import com.simple.gameframe.core.listener.EventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class RoomEventPublisher implements EventPublisher {

    private final List<EventListener> listenerList = new ArrayList<>();

    @Override
    public void create(Room<Player> room, Player player, Object o) {
        Event event = new CreateEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void join(Room<Player> room, Player player, Object o) {
        Event event = new JoinEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void canStart(Room<Player> room, Player player, Object o) {
        Event event = new CanStartEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void start(Room<Player> room, Player player, Object o) {
        Event event = new StartEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void turnNext(Room<Player> room, Player player, Object o) {
        Event event = new TurnNextEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void turnRound(Room<Player> room, Player player, Object o) {
        Event event = new TurnRoundEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void voteDismiss(Room<Player> room, Player player, Object o) {
        Event event = new VoteDismissEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void dismiss(Room<Player> room, Player player, Object o) {
        Event event = new DismissEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void gameOver(Room<Player> room, Player player, Object o) {
        Event event = new GameOverEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void disconnect(Room<Player> room, Player player, Object o) {
        Event event = new DisconnectEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void reconnect(Room<Player> room, Player player, Object o) {
        Event event = new ReconnectEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void timeout(Room<Player> room, Player player, Object o) {
        Event event = new TimeOutEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void gameResult(Room<Player> room, Player player, Object o){
        Event event = new GameResultEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void seatDown(Room<Player> room, Player player, Object o){
        Event event = new SeatDownEvent(room, player, o);
        for (EventListener listener : listenerList) {
            try {
                if (!listener.eventHandle(event)) {
                    break;
                }
            } catch (ClassCastException e) {
                log.trace("跳过不符合的监听事件");
            }
        }
    }

    @Override
    public void addListener(EventListener<?>... listeners) {
        Collections.addAll(this.listenerList, listeners);
    }
}
