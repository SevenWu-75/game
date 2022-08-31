package com.simple.gameframe.core;

import com.simple.api.common.SpeedBootException;
import com.simple.api.game.Message;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.util.MessagePublishUtil;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitStartLogicHandler implements LogicHandler {

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private Message<?> receivedMessage;

    private String roomId;

    private Long userId;

    @Override
    public Lock getLock() {
        return this.lock;
    }

    @Override
    public Condition getCondition() {
        return this.condition;
    }

    @Override
    public Long getWaitTimeSecond() {
        return 60*5L;
    }

    @Override
    public Message<?> getReceivedMessage() {
        return this.receivedMessage;
    }

    @Override
    public void setReceivedMessage(Message<?> message) {
        this.receivedMessage = message;
    }

    @Override
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String getRoomId() {
        return this.roomId;
    }

    @Override
    public boolean handle(Long userId, String roomId) {
        //询问是否开始游戏
        Message<Void> message = new DefaultMessage<>();
        message.setCode(Command.START_GAME.ordinal());
        message.setFromId(getUserId());
        message.setRoomId(getRoomId());
        try{
            ask(userId, roomId, message);
        } catch (SpeedBootException e){
            return false;
        }
        //广播开始游戏
        Message<Void> newMessage = new DefaultMessage<>();
        newMessage.setCode(message.getCode());
        MessagePublishUtil.sendToRoomPublic(roomId, message);
        return true;
    }
}
