package com.simple.mardice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.mardice.bo.MarPlayer;
import com.simple.mardice.common.MarCommand;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component()
@Order(1)
@Slf4j
public class PlayDiceLogicHandler implements LogicHandler<MarCommand> {

    @Autowired
    private SelectOrEndLogicHandler selectOrEndLogicHandler;

    @Override
    public List<MarCommand> getCommands() {
        return Collections.singletonList(MarCommand.PLAY_DICE);
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return null;
    }

    @Override
    public Message<?> messageHandle(Player player, Room<? extends Player> room, Object o) {
        Message<Integer> message = new DefaultMessage<>();
        message.setCode(MarCommand.PLAY_DICE.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setRoomId(room.getRoomId());
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<? extends Player> room, Message<?> message, Object o) {
        MarPlayer marPlayer = (MarPlayer) player;
        marPlayer.playDices();
        sendDiceResultToPublic(marPlayer, room.getRoomId());
        return o;
    }

    @Override
    public LogicHandler<?> getNextHandler() {
        return selectOrEndLogicHandler;
    }

    private void sendDiceResultToPublic(@NotNull MarPlayer player, String roomId){
        Message<MarPlayer> message = new DefaultMessage<>();
        message.setRoomId(roomId);
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setContent(player);
        message.setCode(MarCommand.PLAY_DICE.getCode());
        MessagePublishUtil.sendToRoomPublic(roomId, message);
    }
}
