package com.simple.gameframe.core.ask;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.gameframe.util.RoomPropertyManagerUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StartLogicHandler implements LogicHandler<GameCommand> {

    @Override
    public List<GameCommand> getCommands(){
        return Collections.singletonList(GameCommand.START_GAME);
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return null;
    }

    @Override
    public Message<?> messageHandle(Player player, Room<Player> room, Object o) {
        //询问是否开始游戏
        Message<Void> message = new DefaultMessage<>();
        message.setCode(GameCommand.START_GAME.getCode());
        message.setFromId(player.getUser().getId());
        message.setRoomId(room.getRoomId());
        message.setSeat(player.getId());
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<Player> room, Message<?> message, Object o){
        //广播开始游戏
        Message<Void> newMessage = new DefaultMessage<>();
        newMessage.setCode(GameCommand.START_GAME.getCode());
        MessagePublishUtil.sendToRoomPublic(room.getRoomId(), newMessage);
        return null;
    }

    @Override
    public LogicHandler<?> getNextHandler() {
        return null;
    }
}
