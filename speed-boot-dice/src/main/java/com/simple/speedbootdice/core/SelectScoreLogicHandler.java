package com.simple.speedbootdice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.speedbootdice.common.SpeedBootCommand;
import com.simple.speedbootdice.pojo.SpeedBootPlayer;
import com.simple.speedbootdice.vo.DiceResultVo;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(1)
public class SelectScoreLogicHandler implements LogicHandler<SpeedBootCommand> {

    @Autowired
    LogicHandler<SpeedBootCommand> playDiceLogicHandler;

    private final ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    private LogicHandler<?> nextHandler;

    @Override
    public SpeedBootCommand getCommand(){
        return SpeedBootCommand.SELECT_SCORE;
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return null;
    }

    @Override
    public Message<?> messageHandle(@NotNull Player player, @NotNull Room<? extends Player> room, Object o) {
        Message<Integer> message = new DefaultMessage<>();
        message.setCode(SpeedBootCommand.SELECT_SCORE.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setRoomId(room.getRoomId());
        message.setContent(((SpeedBootPlayer)player).getPlayTimes());
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<? extends Player> room, Message<?> message, Object o) {
        SpeedBootPlayer sp = (SpeedBootPlayer) player;
        //如果是继续投骰子
        if(message.getCode() == SpeedBootCommand.ANSWER_DICE.getCode()){
            if(sp.enoughPlayTimes())
                setNextHandler(playDiceLogicHandler);
            return message.getContent();
        } else {
            int index = (Integer) message.getContent();
            if(o instanceof DiceResultVo){
                DiceResultVo diceResultVo = (DiceResultVo) o;
                int score = diceResultVo.getScores()[index];
                sp.updateScores(index,score);
                //广播玩家投掷骰子结果 =====》
                sendSelectScoreResultToPublic(sp, room.getRoomId());
                //重置玩家骰子次数
                sp.resetPlayTimes();
            }
        }
        return null;
    }

    private void sendSelectScoreResultToPublic(@NotNull SpeedBootPlayer player, String roomId){
        Message<int[]> message = new DefaultMessage<>();
        message.setCode(SpeedBootCommand.SELECT_SCORE.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setRoomId(roomId);
        message.setContent(player.getScores());
        MessagePublishUtil.sendToRoomPublic(roomId, message);
    }

    @Override
    public void setNextHandler(LogicHandler logicHandler) {
        this.nextHandler = logicHandler;
    }

    @Override
    public LogicHandler getNextHandler() {
        return this.nextHandler;
    }
}
