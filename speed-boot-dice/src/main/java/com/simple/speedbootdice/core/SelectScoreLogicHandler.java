package com.simple.speedbootdice.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.speedbootdice.common.SpeedBootCommand;
import com.simple.speedbootdice.bo.SpeedBootPlayer;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(3)
public class SelectScoreLogicHandler implements LogicHandler<SpeedBootCommand> {

    private final ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    @Override
    public List<SpeedBootCommand> getCommands(){
        return Arrays.asList(SpeedBootCommand.SELECT_SCORE, SpeedBootCommand.PLAY_DICE);
    }

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return this.receivedMessageMap;
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
        int index = (Integer) message.getContent();
        if(o instanceof SpeedBootPlayer){
            SpeedBootPlayer playerVO = (SpeedBootPlayer) o;
            int score = playerVO.getCurrentScores()[index];
            sp.updateScores(index,score);
            //广播玩家投掷骰子结果 =====》
            sendSelectScoreResultToPublic(sp, room.getRoomId());
            //重置玩家骰子次数
            sp.resetPlayTimes();
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
    public LogicHandler<?> getNextHandler() {
        return null;
    }
}
