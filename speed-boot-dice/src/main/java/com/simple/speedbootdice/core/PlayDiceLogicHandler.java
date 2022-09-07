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
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component()
@Order(1)
@Slf4j
public class PlayDiceLogicHandler implements LogicHandler<SpeedBootCommand> {

    @Autowired
    LogicHandler<SpeedBootCommand> selectScoreLogicHandler;

    private final ConcurrentHashMap<String, Message<?>> receivedMessageMap = new ConcurrentHashMap<>();

    @Override
    public ConcurrentHashMap<String, Message<?>> getReceivedMessageMap() {
        return receivedMessageMap;
    }

    @Override
    public List<SpeedBootCommand> getCommands(){
        return Collections.singletonList(SpeedBootCommand.PLAY_DICE);
    }

    @Override
    public Message<?> messageHandle(Player player, Room<? extends Player> room, Object o) {
        Message<Integer> message = new DefaultMessage<>();
        message.setCode(SpeedBootCommand.PLAY_DICE.getCode());
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setRoomId(room.getRoomId());
        message.setContent(3);
        return message;
    }

    @Override
    public Object postHandle(Player player, Room<? extends Player> room, Message<?> message, Object o) {
        int[] lockDice = new int[]{-1,-1,-1,-1,-1};
        if(o instanceof int[]){
            lockDice = (int[]) o;
        }

        return playDiceLogic((SpeedBootPlayer) player, lockDice, room.getRoomId());
    }

    @Override
    public LogicHandler<?> getNextHandler() {
        return selectScoreLogicHandler;
    }

    private DiceResultVo playDiceLogic(@NotNull SpeedBootPlayer player, int[] lockDice, String roomId){
        //抛骰子次数减少1
        player.costPlayTimes();
        log.debug("询问{}抛骰子，次数为{}",player.getUser().getId(), player.getPlayTimes());
        List<Integer> diceList = player.playDices(lockDice);
        DiceResultVo diceResultVo = new DiceResultVo();
        diceResultVo.setNumbers(diceList);
        diceResultVo.setScores(calculate(diceList));
        diceResultVo.setTimes(player.getPlayTimes());
        diceResultVo.setHaveScores(player.getScores());
        //广播玩家投掷骰子结果 =====》
        sendDiceResultToPublic(player, roomId, diceResultVo);
        return diceResultVo;
    }

    private void sendDiceResultToPublic(@NotNull SpeedBootPlayer player, String roomId, DiceResultVo diceList){
        Message<DiceResultVo> message = new DefaultMessage<>();
        message.setRoomId(roomId);
        message.setFromId(player.getUser().getId());
        message.setSeat(player.getId());
        message.setContent(diceList);
        message.setCode(SpeedBootCommand.DICE_RESULT.getCode());
        MessagePublishUtil.sendToRoomPublic(roomId, message);
    }

    @NotNull
    @Contract("_ -> new")
    private int[] calculate(@NotNull List<Integer> numbers) {
        int oneSum = 0;
        int twoSum = 0;
        int threeSum = 0;
        int fourSum = 0;
        int fiveSum = 0;
        int sixSum = 0;
        int sum = 0;
        int kind4 = 0;
        int fullHouse = 0;
        int smallStraight = 0;
        int straight = 0;
        int Yahtzee = 0;
        for (int number : numbers) {
            if (number == 1) {
                oneSum += number;
            }
            if (number == 2) {
                twoSum += number;
            }
            if (number == 3) {
                threeSum += number;
            }
            if (number == 4) {
                fourSum += number;
            }
            if (number == 5) {
                fiveSum += number;
            }
            if (number == 6) {
                sixSum += number;
            }
            sum += number;
        }

        Map<Integer, Long> map = numbers.stream().collect(Collectors.groupingBy(num -> num, Collectors.counting()));
        for (Map.Entry<Integer, Long> entry : map.entrySet()) {
            //只有四骰同花和葫芦两种可能是只有两组的情况
            if(map.size() == 2){
                if(entry.getValue() == 2 || entry.getValue() == 3){
                    fullHouse += entry.getKey() * entry.getValue();
                } else {
                    kind4 += entry.getKey() * entry.getValue();
                }
            }
            //一组的情况是快艇、四骰同花、葫芦
            if(map.size() == 1){
                Yahtzee = 50;
                fullHouse += entry.getKey() * entry.getValue();
                kind4 += entry.getKey() * entry.getValue();
            }
            if(map.size() >= 4){
                Set<Integer> integers = map.keySet();
                if((integers.contains(1) && integers.contains(2) && integers.contains(3) && integers.contains(4))
                        || (integers.contains(2) && integers.contains(3) && integers.contains(4) && integers.contains(5))
                        || (integers.contains(3) && integers.contains(4) && integers.contains(5) && integers.contains(6))){
                    smallStraight = 15;
                }
            }
            if(map.size() == 5){
                Set<Integer> integers = map.keySet();
                if((integers.contains(1)
                        && integers.contains(2)
                        && integers.contains(3)
                        && integers.contains(4)
                        && integers.contains(5)) ||
                        (integers.contains(2)
                                && integers.contains(3)
                                && integers.contains(4)
                                && integers.contains(5)
                                && integers.contains(6))){
                    straight = 30;
                    break;
                }
            }
        }
        return new int[]{oneSum,twoSum,threeSum,fourSum,fiveSum,sixSum,sum,kind4,fullHouse,smallStraight,straight,Yahtzee};
    }
}
