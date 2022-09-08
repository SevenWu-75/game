package com.simple.speedbootdice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.speedbootdice.common.SpeedBootCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(2)
public class PlayDiceOrSelectScoreLogicHandler implements LogicHandler<SpeedBootCommand> {

    private LogicHandler<SpeedBootCommand> nextHandler;

    @Autowired
    LogicHandler<SpeedBootCommand> playDiceLogicHandler;

    @Autowired
    LogicHandler<SpeedBootCommand> selectScoreLogicHandler;

    private final ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    @Override
    public List<SpeedBootCommand> getCommands() {
        return Collections.singletonList(SpeedBootCommand.PLAY_DICE_OR_SELECT_SCORE);
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return this.receivedMessageMap;
    }

    @Override
    public Message<?> messageHandle(Player player, Room<? extends Player> room, Object o) {
        Message<Void> message = new DefaultMessage<>();
        message.setRoomId(room.getRoomId());
        message.setCode(SpeedBootCommand.PLAY_DICE_OR_SELECT_SCORE.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<? extends Player> room, Message<?> message, Object o) {
        Object content = message.getContent();
        if(content instanceof Integer){
            Integer code = (Integer) content;
            if(code.equals(SpeedBootCommand.PLAY_DICE.getCode())){
                nextHandler = playDiceLogicHandler;
            } else {
                nextHandler = selectScoreLogicHandler;
            }
        }
        return LogicHandler.super.postHandle(player, room, message, o);
    }

    @Override
    public LogicHandler<?> getNextHandler() {
        return this.nextHandler;
    }
}
