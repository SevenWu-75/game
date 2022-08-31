package com.simple.gameframe.core;

import com.simple.api.common.SpeedBootException;
import com.simple.api.common.SpeedBootExceptionEnum;
import com.simple.api.game.Message;
import com.simple.gameframe.util.MessagePublishUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public interface LogicHandler {

    Lock getLock();

    Condition getCondition();

    Long getWaitTimeSecond();

    Message<?> getReceivedMessage();

    void setReceivedMessage(Message<?> message);

    boolean handle(Long userId, String roomId);

    default Message<?> ask(Long userId, String roomId, Message<?> message) {
        getLock().lock();
        try {
            MessagePublishUtil.sendToRoomUser(String.valueOf(userId), roomId, message);
            Long waitTime = getWaitTimeSecond();
            if(waitTime != null){
                boolean await = getCondition().await(waitTime, TimeUnit.SECONDS);
                if(!await){
                    throw new SpeedBootException(SpeedBootExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
                }
            } else {
                getCondition().await();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            getLock().unlock();
        }
        return getReceivedMessage();
    }

    default void answer(Message<?> message) {
        getLock().lock();
        try {
            setReceivedMessage(message);
            getCondition().signal();
        } finally {
            getLock().unlock();
        }
    }
}
