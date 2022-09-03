package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.exception.GameException;
import com.simple.gameframe.core.ask.AskAnswerLockConditionManager;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.core.ask.LogicHandlerProcessor;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.util.ApplicationContextUtil;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Builder
public class RoundHandlerProcessor implements RoundHandler {

    LogicHandlerProcessor logicHandlerProcessor;

    Room room;

    SeatHandler seatHandler;

    private LogicHandler startLogicHandler;

    @Override
    public LogicHandlerProcessor getLogicHandlerProcessor() {
        return this.logicHandlerProcessor;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    public RoundHandlerProcessor(LogicHandlerProcessor logicHandlerProcessor, SeatHandler seatHandler){
        this.logicHandlerProcessor = logicHandlerProcessor;
        this.seatHandler = seatHandler;
    }

    @Override
    public void startLogic(@NotNull Room room) {
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        final List<Player> playerList = room.getPlayerList();
        preHandle(playerList.get(0),room,AskAnswerLockConditionManager.getLock(room.getRoomId()));
        //开始游戏
        eventPublisher.start(room, null);
        room.start();
        Object roundResult = null;
        for (int i = 0; i < room.getPlayCount(); i++) {
            //开始回合
            eventPublisher.turnRound(room,i);
            roundResult = round(room, seatHandler.getRoundPlayer(i, playerList, roundResult));
        }
        eventPublisher.gameOver(room,roundResult);
        room.end();
    }

    @Override
    public Object round(Room room, List<Player> players) {
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        Object o = null;
        for (Player player : players) {
            //开始换人
            eventPublisher.turnNext(room, player);
            o = handle(player, room, AskAnswerLockConditionManager.getLock(room.getRoomId()));
        }
        return o;
    }

    private void preHandle(Player player, Room room, Lock lock){
        if(startLogicHandler != null){
            startLogicHandler.preHandle(player, room, null);
            Message<?> sendMessage = startLogicHandler.messageHandle(player, room, null);
            Message<?> receiveMessage = startLogicHandler.ask(player.getUser().getId(), room, sendMessage, lock,
                    AskAnswerLockConditionManager.getCondition(room.getRoomId(), startLogicHandler.toString()));
            startLogicHandler.postHandle(player, room, receiveMessage, null);
        }
    }


    public Object handle(Player player, Room room, Lock lock) {
        return getLogicHandlerProcessor().handle(player, room, lock);
    }
}
