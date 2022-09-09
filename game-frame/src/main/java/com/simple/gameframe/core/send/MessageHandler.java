package com.simple.gameframe.core.send;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import org.jetbrains.annotations.NotNull;

public interface MessageHandler {

    Message<?> messageHandle(Room<? extends Player> room, Player player, Object o);

    default void sendRoomPublic(@NotNull Room<? extends Player> room, Message<?> message){
        MessagePublishUtil.sendToRoomPublic(room.getRoomId(), message);
    }
}
