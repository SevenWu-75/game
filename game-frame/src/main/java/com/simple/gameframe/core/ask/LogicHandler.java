package com.simple.gameframe.core.ask;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.GameException;
import com.simple.gameframe.common.GameExceptionEnum;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface LogicHandler {

    ConcurrentHashMap<String, Message<?>> getReceivedMessageMap();

    default Long getWaitTimeSecond() {
        return 60*5L;
    }

    default boolean preHandle(Player player, Room room, Object o) {
        return true;
    }

    Message<?> messageHandle(Player player, Room room, Object o);

    default Message<?> ask(Long userId, @NotNull Room room, Message<?> message, @NotNull Lock lock, Condition condition) {
        lock.lock();
        try {
            MessagePublishUtil.sendToRoomUser(String.valueOf(userId), room.getRoomId(), message);
            Long waitTime = getWaitTimeSecond();
            if(waitTime != null){
                boolean await = condition.await(waitTime, TimeUnit.SECONDS);
                if(!await){
                    throw new GameException(GameExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
                }
            } else {
                condition.await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return getReceivedMessageMap().get(room.getRoomId());
    }

    default Object postHandle(Player player, Room room, Message<?> message, Object o) {
        return null;
    }

    default void answer(@NotNull Lock lock, @NotNull Room room, @NotNull Condition condition, Message<?> message) {
        lock.lock();
        try {
            getReceivedMessageMap().put(room.getRoomId(), message);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    default void setNextHandler(LogicHandler logicHandler){

    }

    LogicHandler getNextHandler();
}
