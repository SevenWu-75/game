package com.simple.mardice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Dice;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.mardice.bo.MarPlayer;
import com.simple.mardice.bo.MarRoom;
import com.simple.mardice.common.DiceNumEnum;
import com.simple.mardice.common.MarCommand;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component()
@Order(1)
@Slf4j
public class SelectOrEndLogicHandler implements LogicHandler<MarCommand> {

    private final ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    private LogicHandler<MarCommand> nextHandler;

    @Autowired
    private PlayDiceLogicHandler playDiceLogicHandler;

    @Override
    public List<MarCommand> getCommands() {
        return Arrays.asList(MarCommand.END_ROUND,MarCommand.SELECT_DICE);
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return receivedMessageMap;
    }

    @Override
    public Message<?> messageHandle(@NotNull Player player, @NotNull Room<? extends Player> room, Object o) {
        Message<Integer> message = new DefaultMessage<>();
        message.setCode(MarCommand.SELECT_OR_END.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setRoomId(room.getRoomId());
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<? extends Player> room, @NotNull Message<?> message, Object o) {
        if(message.getCode() == MarCommand.SELECT_DICE.getCode()){
            ((MarPlayer)player).selectDice((int)o);
            nextHandler = playDiceLogicHandler;
        } else if(message.getCode() == MarCommand.END_ROUND.getCode()) {
            processScore((MarPlayer) player, (MarRoom) room);
            nextHandler = null;
        }
        return o;
    }

    @Override
    public LogicHandler<?> getNextHandler() {
        return nextHandler;
    }

    public void processScore(@NotNull MarPlayer player, MarRoom room) {
        int currentScore = player.getCurrentScore();
        player.getScoreList().add(currentScore);
        if(player.getTotalScore() >= 25){
            room.setPlayCount(0);
        }
        player.resetDice();
    }
}
