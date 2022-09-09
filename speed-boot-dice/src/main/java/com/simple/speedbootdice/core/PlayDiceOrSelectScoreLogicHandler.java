package com.simple.speedbootdice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.speedbootdice.common.SpeedBootCommand;
import com.simple.speedbootdice.pojo.SpeedBootPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class PlayDiceOrSelectScoreLogicHandler implements LogicHandler<SpeedBootCommand> {

    private LogicHandler<?> nextHandler = this;

    @Autowired
    LogicHandler<SpeedBootCommand> playDiceLogicHandler;

    @Autowired
    LogicHandler<SpeedBootCommand> selectScoreLogicHandler;

    private final ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    @Override
    public List<SpeedBootCommand> getCommands() {
        return Arrays.asList(SpeedBootCommand.PLAY_DICE, SpeedBootCommand.SELECT_SCORE);
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return this.receivedMessageMap;
    }

    @Override
    public Message<?> messageHandle(Player player, Room<? extends Player> room, Object o) {
        Message<Object> message = new DefaultMessage<>();
        message.setRoomId(room.getRoomId());
        message.setCode(SpeedBootCommand.PLAY_DICE_OR_SELECT_SCORE.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        SpeedBootPlayer sp = (SpeedBootPlayer) player;
        message.setContent(sp.getPlayTimes());
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<? extends Player> room, Message<?> message, Object o) {
        Object result = o;
        if(message.getCode() == SpeedBootCommand.PLAY_DICE.getCode()){
            result = playDiceLogicHandler.postHandle(player, room, message, o);
            nextHandler = this;
        } else {
            result = selectScoreLogicHandler.postHandle(player,room,message,o);
            nextHandler = null;
        }
        return result;
    }

    @Override
    public LogicHandler<?> getNextHandler() {
        return this.nextHandler;
    }
}
