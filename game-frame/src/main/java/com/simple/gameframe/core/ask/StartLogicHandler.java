package com.simple.gameframe.core.ask;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;

import java.util.concurrent.ConcurrentHashMap;

public class StartLogicHandler implements LogicHandler {

    private ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return this.receivedMessageMap;
    }

    @Override
    public Message<?> messageHandle(Player player, Room room, Object o) {
        //询问是否开始游戏
        Message<Void> message = new DefaultMessage<>();
        message.setCode(Command.START_GAME.getCode());
        message.setFromId(player.getUser().getId());
        message.setRoomId(room.getRoomId());
        message.setSeat(player.getId());
        return message;
    }

    @Override
    public Object postHandle(Player player, Room room, Message<?> message, Object o){
        //广播开始游戏
        Message<Void> newMessage = new DefaultMessage<>();
        newMessage.setCode(Command.START_GAME.getCode());
        MessagePublishUtil.sendToRoomPublic(room.getRoomId(), newMessage);
        return null;
    }

    @Override
    public LogicHandler getNextHandler() {
        return null;
    }
}
