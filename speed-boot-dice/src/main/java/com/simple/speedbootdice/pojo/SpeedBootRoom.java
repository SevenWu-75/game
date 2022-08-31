package com.simple.speedbootdice.pojo;

import com.simple.api.common.SpeedBootException;
import com.simple.api.common.SpeedBootExceptionEnum;
import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadPoolUtil;
import com.simple.gameframe.util.ApplicationContextUtil;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.gameframe.util.RoomManagerUtil;
import com.simple.speedbootdice.common.Command;
import com.simple.speedbootdice.common.ScoreEnum;
import com.simple.speedbootdice.service.HistoryRankService;
import com.simple.speedbootdice.util.*;
import com.simple.speedbootdice.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class SpeedBootRoom implements Room, Serializable {

    private final String roomId;

    private final User owner;

    private int roomStatus;

    private final Date createTime;

    private Date startTime;

    private Date endTime;

    private final LinkedList<Player> playerList;

    private final Set<User> onlooker;

    private final static Random random = new Random();

    private final int playAtLeastNum;

    private final AskAnswerUtil askAnswerUtil;

    private final MessagePublishUtil messagePublishUtil;

    private final AtomicInteger playCount;

    private Iterator<Player> playerListIterator;

    public SpeedBootRoom(User owner){
        messagePublishUtil = ApplicationContextUtil.getBean(MessagePublishUtil.class);
        askAnswerUtil = new AskAnswerUtil(messagePublishUtil);
        this.roomId = String.valueOf(generateRoomId());
        askAnswerUtil.setRoomId(roomId);
        this.owner = owner;
        this.onlooker = new HashSet<>();
        this.createTime = new Date();
        this.playerList = new LinkedList<>();
        this.playAtLeastNum = 2;
        this.playCount = new AtomicInteger(12);
    }

    @Override
    public String getRoomId() {
        return this.roomId;
    }

    @Override
    public User getOwner() {
        return this.owner;
    }

    @Override
    public int getRoomStatus() {
        return this.roomStatus;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public Date getStartTime() {
        return this.startTime;
    }

    @Override
    public Date getEndTime() {
        return this.endTime;
    }

    @Override
    public LinkedList<Player> getPlayerList() {
        return this.playerList;
    }

    @Override
    public Set<User> getOnlooker() {
        return this.onlooker;
    }

    public void start(){
        this.startTime = new Date();
        this.roomStatus = 1;
        playerListIterator = playerList.listIterator();
    }

    public void end(){
        this.endTime = new Date();
        this.roomStatus = 2;
    }

    private int generateRoomId(){
        return random.nextInt(100000) + random.nextInt(9) * 100000 + 100000;
    }

    @Override
    public void join(User user){
        checkUser(user.getId());
        this.onlooker.add(user);
    }

    public int seatDown(User user){
        checkUser(userVo.getId());
        SpeedBootPlayer player = new SpeedBootPlayer(generatePlayerId(), user);
        playerList.add(player);
        return player.getId();
    }

    private int generatePlayerId(){
        return playerList.stream().map(SpeedBootPlayer::getId).max(Integer::compare).map(integer -> integer + 1).orElse(0);
    }

    private void checkUser(Long userId){
        Set<Long> userIdSet = playerList.stream().map(p->p.getUserVo().getId()).collect(Collectors.toSet());
        if(userIdSet.contains(userId))
            throw new SpeedBootException(SpeedBootExceptionEnum.SEAT_REPEATED);
    }
    /**
     * 整体游戏进行问答机制的逻辑流程
     *
     */
    private void playLogic(){
        try{
            //人员未齐先进行开局前的阻塞
            askAnswerUtil.awaitStart();
            //广播游戏开始 =====》
            //开始游戏，进行各种标记
            log.debug("符合最低人数要求，准备开始游戏");
            askAnswerUtil.askStart(owner.getId());
            start();
            SpeedBootPlayer player = turnNext(playCount);
            //循环体开始
            while (playCount.get() > 0) {
                AtomicInteger playTimes = new AtomicInteger(3);
                DiceResultVo diceResultVo = null;
                askAnswerUtil.askPlayDice(player);
                boolean isContinue = false;
                int[] lockDice = new int[]{-1,-1,-1,-1,-1};
                do {
                    //私发玩家投掷骰子 =====》 收到玩家投掷骰子 《=====
                    diceResultVo = playDiceLogic(player, playTimes, lockDice);
                    //询问玩家选择哪个分数 =====》 收到玩家选择的分数 《=====
                    lockDice = selectScoreOrContinuePlayDiceLogic(player, diceResultVo, playTimes);
                    isContinue = lockDice != null;
                } while (isContinue && playTimes.get() != 0);
                player = turnNext(playCount);
            }
            //循环体结束
            //广播胜利玩家
            sendGameOverResultToPublic();
        } catch (SpeedBootException e){
            //玩家未响应时间超时则解散房间
            boolean timeout = e.getCode().equals(SpeedBootExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
            if(timeout){
                sendRoomTimeout();
            }
        } finally {
            closeRoom();
        }
    }

    private DiceResultVo playDiceLogic(@NotNull SpeedBootPlayer player, AtomicInteger playTimes, int[] lockDice){
        log.debug("询问{}抛骰子，次数为{}",player.getUserVo().getId(),playTimes.get());
        List<Integer> diceList = player.playDices(lockDice);
        DiceResultVo diceResultVo = new DiceResultVo();
        diceResultVo.setNumbers(diceList);
        diceResultVo.setScores(calculate(diceList));
        diceResultVo.setTimes(playTimes.decrementAndGet());
        diceResultVo.setHaveScores(player.getScores());
        //广播玩家投掷骰子结果 =====》
        sendDiceResultToPublic(player, diceResultVo);
        return diceResultVo;
    }

    private void sendDiceResultToPublic(@NotNull SpeedBootPlayer player, DiceResultVo diceList){
        SpeedBootMessage<DiceResultVo> message = new SpeedBootMessage<>();
        message.setRoomId(roomId);
        message.setFromId(player.getUserVo().getId());
        message.setSeat(player.getId());
        message.setContent(diceList);
        message.setCode(Command.ANSWER_DICE.ordinal());
        messagePublishUtil.sendToRoomPublic(roomId, message);
    }

    private int[] selectScoreOrContinuePlayDiceLogic(@NotNull SpeedBootPlayer player, @NotNull DiceResultVo diceResultVo, AtomicInteger playTimes){
        log.debug("询问玩家{}选择分数还是继续抛骰子",player.getUserVo().getId());
        SpeedBootMessage<?> resMessage = askAnswerUtil.askSelectScore(player,playTimes);
        //如果是继续投骰子
        if(resMessage.getCode() == Command.ANSWER_DICE.ordinal()){
            return (int[])resMessage.getContent();
        } else {
            int index = (Integer) resMessage.getContent();
            int score = diceResultVo.getScores()[index];
            player.updateScores(index,score);
            //广播玩家投掷骰子结果 =====》
            sendSelectScoreResultToPublic(player);
        }
        return null;
    }

    private void sendSelectScoreResultToPublic(@NotNull SpeedBootPlayer player){
        SpeedBootMessage<int[]> message = new SpeedBootMessage<>();
        message.setCode(Command.SELECT_SCORE.ordinal());
        message.setFromId(player.getUserVo().getId());
        message.setSeat(player.getId());
        message.setRoomId(roomId);
        message.setContent(player.getScores());
        messagePublishUtil.sendToRoomPublic(roomId, message);
    }

    private void sendGameOverResultToPublic(){
        SpeedBootMessage<GameResultVo> message = new SpeedBootMessage<>();
        message.setRoomId(roomId);
        message.setCode(Command.GAME_OVER.ordinal());
        SpeedBootPlayer winner = null;
        Optional<SpeedBootPlayer> winnerOptional = playerList.stream().max(Comparator.comparing(player -> player.getScores()[ScoreEnum.TOTAL_SUM.ordinal()]));
        if(winnerOptional.isPresent()){
            long count = playerList.stream().filter(
                            player -> player.getScores()[ScoreEnum.TOTAL_SUM.ordinal()] == winnerOptional.get().getScores()[ScoreEnum.TOTAL_SUM.ordinal()])
                    .count();
            if(count == 1){
                //非平局
                winner = winnerOptional.get();
            }
            message.setContent(new GameResultVo(winner, playerList));
        }
        log.debug("广播游戏结束，{}", winner == null ? "平局" : "赢家为" + winner.getUserVo().getRealname());
        messagePublishUtil.sendToRoomPublic(roomId, message);
        //保存历史记录
        for (SpeedBootPlayer player : playerList) {
            historyRankService.saveHistory(player.getUserVo().getId(), 1,
                    winner != null && winner.getUserVo().getId().equals(player.getUserVo().getId()),
                    player.getScores()[ScoreEnum.TOTAL_SUM.ordinal()]);
        }
    }

    private void sendRoomTimeout(){
        SpeedBootMessage<GameResultVo> message = new SpeedBootMessage<>();
        message.setRoomId(roomId);
        message.setCode(Command.TIME_OUT.ordinal());
        log.info("房间{}超时结束", roomId);
        messagePublishUtil.sendToRoomPublic(roomId, message);
    }

    private void sendTurnNextMessage(Integer seat, Integer playerCount){
        SpeedBootMessage<Integer> message = new SpeedBootMessage<>();
        message.setCode(Command.TURN_NEXT.ordinal());
        message.setContent(playerCount);
        message.setSeat(seat);
        messagePublishUtil.sendToRoomPublic(roomId, message);
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

    public RoomVo toRoomVo(){
        return new RoomVo().setStartTime(startTime)
                .setRoomStatus(roomStatus).setCreateBy(owner)
                .setCreateTime(createTime).setRoomId(roomId).setPlayers(playerList)
                .setOnlooker(new ArrayList<>(onlooker));
    }

    @Override
    public void closeRoom(){
        log.trace("房间{}已关闭",this.roomId);
        RoomManagerUtil.removeRoom(this.roomId);
    }

    @Override
    public void dismissRoom(){
        log.trace("房间{}已解散",this.roomId);
        SpeedBootMessage<Void> message = new SpeedBootMessage<>();
        message.setCode(Command.DISMISS_ROOM.ordinal());
        messagePublishUtil.sendToRoomPublic(roomId, message);
    }

    public SpeedBootPlayer turnNext(AtomicInteger playCount){
        //切换下一个玩家
        if (!playerListIterator.hasNext()) {
            playerListIterator = playerList.listIterator();
            playCount.decrementAndGet();
        }
        SpeedBootPlayer next = playerListIterator.next();
        sendTurnNextMessage(next.getId(), playerList.size());
        return next;
    }

    public void sendPublicDisconnectMessage(Long userId){
        Optional<SpeedBootPlayer> first = playerList.stream().filter(player -> player.getUserVo().getId().equals(userId)).findFirst();
        if(first.isPresent()){
            SpeedBootMessage<Long> message = new SpeedBootMessage<>();
            message.setCode(Command.DISCONNECT.ordinal());
            message.setContent(userId);
            message.setSeat(first.get().getId());
            messagePublishUtil.sendToRoomPublic(roomId, message);
        }
    }

    public void sendPublicConnectMessage(Long userId){
        Optional<SpeedBootPlayer> first = playerList.stream().filter(player -> player.getUserVo().getId().equals(userId)).findFirst();
        if(first.isPresent()){
            SpeedBootMessage<Long> message = new SpeedBootMessage<>();
            message.setCode(Command.CONNECT.ordinal());
            message.setContent(userId);
            message.setSeat(first.get().getId());
            messagePublishUtil.sendToRoomPublic(roomId, message);
        }
    }

    @Override
    public void run() {
        playLogic();
    }
}
