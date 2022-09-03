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
    public void create(Room<? extends Player> room, Object o) {
        Event event = new CreateEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void join(Room<? extends Player> room, Object o) {
        Event event = new JoinEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void canStart(Room<? extends Player> room, Object o) {
        Event event = new CanStartEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void start(Room<? extends Player> room, Object o) {
        Event event = new StartEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void turnNext(Room<? extends Player> room, Object o) {
        Event event = new TurnNextEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void turnRound(Room<? extends Player> room, Object o) {
        Event event = new TurnRoundEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void voteDismiss(Room<? extends Player> room, Object o) {
        Event event = new VoteDismissEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void dismiss(Room<? extends Player> room, Object o) {
        Event event = new DismissEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void gameOver(Room<? extends Player> room, Object o) {
        Event event = new GameOverEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void disconnect(Room<? extends Player> room, Object o) {
        Event event = new DisconnectEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void reconnect(Room<? extends Player> room, Object o) {
        Event event = new ReconnectEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void timeout(Room<? extends Player> room, Object o) {
        Event event = new TimeOutEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void gameResult(Room<? extends Player> room, Object o){
        Event event = new GameResultEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void seatDown(Room<? extends Player> room, Object o){
        Event event = new SeatDownEvent(room, o);
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void addListener(EventListener<?>... listeners) {
        Collections.addAll(this.listenerList, listeners);
    }
}
