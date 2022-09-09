package com.simple.gameframe.core.send;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;

public class DefaultMessageHandler implements MessageHandler {

    private GameCommand command;

    public void setCommand(GameCommand command){
        this.command = command;
    }

    @Override
    public Message<?> messageHandle(Room<? extends Player> room, Player player, Object o) {
        Message<Object> message = new DefaultMessage<>();
        message.setRoomId(room.getRoomId());
        message.setCode(command.getCode());
        if(player != null)
            message.setSeat(player.getId());
        message.setContent(o);
        return message;
    }
}
