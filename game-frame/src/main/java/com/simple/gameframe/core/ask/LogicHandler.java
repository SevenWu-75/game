package com.simple.gameframe.core.ask;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface LogicHandler<T extends Command> {

    /**
     * 用于通过指令枚举获取本对象
     *
     * @return 返回本对象属于的指令
     */
    T getCommand();

    /**
     * 如果需要ask后获取answer返回的message，则需要重写这个方法
     *
     * @return 返回一个有对象ConcurrentHashMap
     */
    default ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return null;
    }

    default Long getWaitTimeSecond() {
        return 60*5L;
    }

    default boolean preHandle(Player player, Room<? extends Player> room, Object o) {
        return true;
    }

    Message<?> messageHandle(Player player, Room<? extends Player> room, Object o);

    default Message<?> ask(Long userId, @NotNull Room<? extends Player> room, Message<?> message, @NotNull Lock lock, Condition condition) {
        lock.lock();
        try {
            message.setId(RoomPropertyManagerUtil.incrementAndGetPackageId(room.getRoomId(), this.toString()));
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
        if(getReceivedMessageMap() == null){
            return null;
        }
        return getReceivedMessageMap().get(room.getRoomId());
    }

    default Object postHandle(Player player, Room<? extends Player> room, Message<?> message, Object o) {
        return null;
    }

    default void answer(@NotNull Lock lock, @NotNull Room<? extends Player> room, @NotNull Condition condition, Message<?> message) {
        lock.lock();
        try {
            if(message != null && message.getId() == RoomPropertyManagerUtil.getPackageIdMap(room.getRoomId(), this.toString())){
                if(getReceivedMessageMap() != null)
                    getReceivedMessageMap().put(room.getRoomId(), message);
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    default void setNextHandler(LogicHandler<?> logicHandler){

    }

    LogicHandler<?> getNextHandler();
}
