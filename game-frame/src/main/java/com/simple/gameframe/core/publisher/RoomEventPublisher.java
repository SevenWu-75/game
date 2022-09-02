package com.simple.gameframe.core.publisher;

import com.simple.api.game.Room;
import com.simple.gameframe.core.event.*;
import com.simple.gameframe.core.listener.EventListener;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RoomEventPublisher implements EventPublisher {

    private List<EventListener> listenerList = new ArrayList<>();

    @Override
    public void create(Room room, Object o) {
        Event event = new CreateEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void join(Room room, Object o) {
        Event event = new JoinEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void canStart(Room room, Object o) {
        Event event = new CanStartEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void start(Room room, Object o) {
        Event event = new StartEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void turnNext(Room room, Object o) {
        Event event = new TurnNextEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void turnRound(Room room, Object o) {
        Event event = new TurnRoundEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void voteDismiss(Room room, Object o) {
        Event event = new VoteDismissEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void dismiss(Room room, Object o) {
        Event event = new DismissEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void gameOver(Room room, Object o) {
        Event event = new GameOverEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void disconnect(Room room, Object o) {
        Event event = new DisconnectEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void reconnect(Room room, Object o) {
        Event event = new ReconnectEvent();
        listenerList.forEach(listener -> {
            try{
                listener.eventHandle(event);
            } catch (ClassCastException e){
                log.trace("跳过不符合的监听事件",e);
            }
        });
    }

    @Override
    public void addListener(EventListener<?> listener) {
        this.listenerList.add(listener);
    }
}
