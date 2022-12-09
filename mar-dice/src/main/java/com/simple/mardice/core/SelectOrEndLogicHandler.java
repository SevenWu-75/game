package com.simple.mardice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.mardice.bo.MarPlayer;
import com.simple.mardice.bo.MarRoom;
import com.simple.mardice.common.MarCommand;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component()
@Order(1)
@Slf4j
public class SelectOrEndLogicHandler implements LogicHandler<MarCommand> {

    private final ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    private LogicHandler<MarCommand> nextHandler;

    private List<MarCommand> commands = new ArrayList<MarCommand>()
    {{add(MarCommand.PLAY_DICE);add(MarCommand.SELECT_DICE);add(MarCommand.END_ROUND);}};

    @Override
    public List<MarCommand> getCommands() {
        return commands;
    }

    public void setCommands(List<MarCommand> commands) {
        this.commands = commands;
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return receivedMessageMap;
    }

    @Override
    public Message<?> messageHandle(@NotNull Player player, @NotNull Room<? extends Player> room, Object o) {
        ((MarPlayer)player).enableDice();
        Message<Player> message = new DefaultMessage<>();
        message.setCode(MarCommand.SELECT_OR_END.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setRoomId(room.getRoomId());
        message.setContent(player);
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<? extends Player> room, @NotNull Message<?> message, Object o) {
        if(message.getCode() == MarCommand.PLAY_DICE.getCode()){
            message.setId(0);
            MessagePublishUtil.sendToRoomPublic(room.getRoomId(), message);
            MarPlayer marPlayer = (MarPlayer) player;
            marPlayer.playDices();
            sendPlayerMessageToPublic(player, room.getRoomId(), MarCommand.PLAY_DICE_RESULT.getCode());
            nextHandler = this;
            marPlayer.enableDice();
            commands.clear();
            commands.add(MarCommand.END_ROUND);
            if(marPlayer.getCanDice()){
                commands.add(MarCommand.SELECT_DICE);
            }
        }
        else if(message.getCode() == MarCommand.SELECT_DICE.getCode()){
            ((MarPlayer)player).selectDice((Integer) message.getContent());
            sendPlayerMessageToPublic(player, room.getRoomId(), MarCommand.SELECT_DICE.getCode());
            nextHandler = this;
            ((MarPlayer)player).enableDice();
            commands.clear();
            commands.add(MarCommand.END_ROUND);
            if(((MarPlayer)player).getCanDice()){
                commands.add(MarCommand.PLAY_DICE);
            }
        } else if(message.getCode() == MarCommand.END_ROUND.getCode()) {
            processScore((MarPlayer) player, (MarRoom) room);
            sendPlayerMessageToPublic(player, room.getRoomId(), MarCommand.END_ROUND.getCode());
            nextHandler = null;
        }
        return o;
    }

    @Override
    public LogicHandler<?> getNextHandler() {
        return nextHandler;
    }

    public void processScore(@NotNull MarPlayer player, MarRoom room) {
        player.calculateScore();
        int currentScore = player.getCurrentScore();
        player.getScoreList().add(currentScore);
        if(player.getTotalScore() >= 25){
            room.setPlayCount(0);
        }
        player.resetDice();
    }
}
