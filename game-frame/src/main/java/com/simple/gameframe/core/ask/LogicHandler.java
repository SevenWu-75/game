package com.simple.gameframe.core.ask;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
    List<T> getCommands();

    /**
     * 如果需要ask后获取answer返回的message，则需要重写这个方法
     *
     * @return 返回一个有对象ConcurrentHashMap
     */
    ConcurrentHashMap<String, Message<?>> getReceivedMessageMap();

    default Long getWaitTimeSecond() {
        return 60*5L;
    }

    /**
     * 处理器的前置条件处理
     *
     * @param player 当前回合的玩家对象
     * @param room 当前房间对象
     * @param o 前一个处理器处理的结果
     * @return 是否继续执行本处理器
     */
    default boolean preHandle(Player player, Room<? extends Player> room, Object o) {
        return true;
    }

    /**
     * 消息包组装方法
     *
     * @param player 当前回合的玩家对象
     * @param room 当前房间对象
     * @param o 前一个处理器处理的结果
     * @return 组装好的消息包
     */
    Message<?> messageHandle(Player player, Room<? extends Player> room, Object o);

    /**
     * 本处理器的默认询问玩家操作实现
     *
     * @param userId 询问玩家的id
     * @param room 当前房间对象
     * @param message 询问的消息体
     * @param lock 本处理器专属用来同步应答机制的锁
     * @param condition 本处理器专属用来等待解锁的条件
     * @return 玩家应答的消息体
     */
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

    /**
     * 本处理器应答完成后的后置处理器
     *
     * @param player 当前回合的玩家对象
     * @param room 当前房间对象
     * @param message 玩家应答的消息体
     * @param o 前一个处理器处理的结果
     * @return 后置处理器处理结果
     */
    default Object postHandle(Player player, Room<? extends Player> room, Message<?> message, Object o) {
        return o;
    }

    /**
     * 本处理器的默认应答实现
     *
     * @param lock 本处理器专属用来同步应答机制的锁
     * @param room 当前房间对象
     * @param condition 本处理器专属用来解锁的条件
     * @param message 玩家应答的消息体
     */
    default void answer(@NotNull Lock lock, @NotNull Room<? extends Player> room, @NotNull Condition condition, Message<?> message) {
        lock.lock();
        try {
            if(getReceivedMessageMap() != null)
                getReceivedMessageMap().put(room.getRoomId(), message);
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取下一个处理器
     * 注：只有在enable-link-logic配置开启时才有效，才会执行链式处理
     *
     * @see GameFrameProperty
     * @return 下一个处理器
     */
    LogicHandler<?> getNextHandler();

    /**
     * 默认实现的广播玩家信息的方法
     *
     * @param player 玩家信息
     * @param roomId 房间号
     * @param code 指令代码
     */
    default void sendPlayerMessageToPublic(@NotNull Player player, String roomId, int code){
        Message<Player> message = new DefaultMessage<>();
        message.setRoomId(roomId);
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setContent(player);
        message.setCode(code);
        MessagePublishUtil.sendToRoomPublic(roomId, message);
    }
}
