package com.simple.gameframe.core.send;

import com.simple.api.game.Room;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;

public class DefaultMessageHandler implements MessageHandler {

    private Command command;

    public void setCommand(Command command){
        this.command = command;
    }

    @Override
    public Message<?> messageHandle(Room room, Object o) {
        Message<Object> message = new DefaultMessage<>();
        message.setRoomId(room.getRoomId());
        message.setCode(command.getCode());
        message.setContent(o);
        return message;
    }
}
