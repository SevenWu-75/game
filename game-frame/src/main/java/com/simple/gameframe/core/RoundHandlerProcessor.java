package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.GameException;
import com.simple.gameframe.common.GameExceptionEnum;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;

@Builder
public class RoundHandlerProcessor implements RoundHandler {

    LogicHandlerProcessor logicHandlerProcessor;

    BiFunction<List<Player>, Integer, Player> nextPlayerFunction;

    Room room;

    Lock lock = new ReentrantLock();

    @Override
    public BiFunction<List<Player>, Integer, Player> getNextPlayerFunction() {
        return this.nextPlayerFunction;
    }

    @Override
    public LogicHandlerProcessor getLogicHandlerProcessor() {
        return this.logicHandlerProcessor;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

    @Override
    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public Lock getLock() {
        return this.lock;
    }

    public RoundHandlerProcessor(LogicHandlerProcessor logicHandlerProcessor){
        this.logicHandlerProcessor = logicHandlerProcessor;
    }

    @Override
    public void startLogic(@NotNull Room room) {
        if(nextPlayerFunction == null){
            throw new GameException(GameExceptionEnum.NO_NEXT_PLAYER_LOGIC);
        }
        Player nextPlayer = room.getPlayerList().get(0);
        for (int i = 0; i < room.getPlayCount() * room.getPlayerList().size(); i++) {
            handle(nextPlayer, room, AskAnswerLockConditionManager.getLock(room.getRoomId()));
            nextPlayer = getNextPlayerFunction().apply(room.getPlayerList(), nextPlayer.getId());
        }
    }

    public void handle(Player player, Room room, Lock lock) {
        getLogicHandlerProcessor().process(player, room, lock);
    }

    @Override
    public void run() {
        final Room currentRoom = this.room;
        this.room = null;
        lock.unlock();
        startLogic(currentRoom);
    }
}
