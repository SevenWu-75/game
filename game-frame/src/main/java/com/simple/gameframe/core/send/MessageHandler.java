package com.simple.gameframe.core.send;

import com.simple.api.game.Room;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import org.jetbrains.annotations.NotNull;

public interface MessageHandler {

    Message<?> messageHandle(Room room, Object o);

    default void sendRoomPublic(@NotNull Room room, Message<?> message){
        MessagePublishUtil.sendToRoomPublic(room.getRoomId(), message);
    }
}
