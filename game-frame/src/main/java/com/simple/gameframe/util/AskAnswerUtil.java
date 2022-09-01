package com.simple.gameframe.util;

import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.core.Message;
import com.simple.speedbootdice.common.Command;
import com.simple.speedbootdice.pojo.Message;
import com.simple.speedbootdice.pojo.Player;
import com.simple.speedbootdice.pojo.Room;
import com.simple.speedbootdice.vo.SeatVo;
import com.simple.speedbootdice.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class AskAnswerUtil {

    private final MessagePublishUtil messagePublishUtil;

    private final CountDownLatch countDownLatch;

    private final ReentrantLock lock;

    private final Condition startCondition;

    private final Condition playDiceCondition;

    private final Condition selectScoreCondition;

    private Message<?> message;

    private String roomId;

    private final static int waitTimeOutToCloseRoom = 5;

    private int messageId;

    public AskAnswerUtil(MessagePublishUtil messagePublishUtil){
        this.messagePublishUtil = messagePublishUtil;
        countDownLatch = new CountDownLatch(2);
        lock = new ReentrantLock();
        startCondition = lock.newCondition();
        playDiceCondition = lock.newCondition();
        selectScoreCondition = lock.newCondition();
    }

    public void askStart(Long id){
        lock.lock();
        try {
            Message<Void> message = new Message<>();
            message.setCode(Command.START_GAME.getCode());
            message.setFromId(id);
            message.setRoomId(roomId);
            log.debug("询问房主{}是否开始",id);
            messagePublishUtil.sendToRoomUser(String.valueOf(id), roomId, message);
            boolean await = startCondition.await(waitTimeOutToCloseRoom, TimeUnit.MINUTES);
            if(!await){
                throw new SpeedBootException(SpeedBootExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public void askPlayDice(@NotNull Player player){
        lock.lock();
        try {
            Message<Integer> message = new Message<>();
            message.setCode(Command.ASK_DICE.getCode());
            message.setFromId(player.getUserVo().getId());
            message.setSeat(player.getId());
            message.setRoomId(roomId);
            message.setContent(3);
            log.debug("询问玩家{}投掷骰子",player.getUserVo().getUsername());
            messagePublishUtil.sendToRoomUser(String.valueOf(player.getUserVo().getId()), roomId, message);
            boolean await = playDiceCondition.await(waitTimeOutToCloseRoom, TimeUnit.MINUTES);
            if(!await) {
                throw new SpeedBootException(SpeedBootExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public Message<?> askSelectScore(@NotNull Player player, @NotNull AtomicInteger playTimes){
        lock.lock();
        try {
            Message<Integer> message = new Message<>();
            message.setCode(Command.SELECT_SCORE.getCode());
            message.setFromId(player.getUserVo().getId());
            message.setSeat(player.getId());
            message.setRoomId(roomId);
            message.setContent(playTimes.get());
            log.debug("询问玩家{}选择分数还是继续",player.getUserVo().getUsername());
            messagePublishUtil.sendToRoomUser(String.valueOf(player.getUserVo().getId()), roomId, message);
            boolean await = playDiceCondition.await(waitTimeOutToCloseRoom, TimeUnit.MINUTES);
            if(!await){
                throw new SpeedBootException(SpeedBootExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return this.message;
    }

    public void answerStart(Message<Void> message){
        lock.lock();
        try {
            log.debug("接收玩家选择开始游戏");
            Message<Void> newMessage = new Message<>();
            newMessage.setCode(message.getCode());
            messagePublishUtil.sendToRoomPublic(roomId, message);
            startCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void answerPlayDice(Message<int[]> message){
        lock.lock();
        try{
            log.debug("接收玩家投掷骰子");
            this.message = message;
            playDiceCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void answerSelectScore(Message<Integer> message){
        lock.lock();
        try {
            log.debug("接收玩家选择分数还是继续");
            this.message = message;
            playDiceCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public void awaitStart(){
        try {
            boolean await = countDownLatch.await(waitTimeOutToCloseRoom, TimeUnit.MINUTES);
            if(!await){
                throw new SpeedBootException(SpeedBootExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void signalSeatDown(){
        Room room = ThreadLocalUtil.getRoom();
        UserVo userVo = ThreadLocalUtil.getUser();
        lock.lock();
        try{
            if(room.getRoomStatus() == 0){
                int seatNum = room.seatDown(userVo);
                Message<SeatVo> message = new Message<>();
                message.setCode(Command.SEAT_DOWN.getCode());
                SeatVo content = new SeatVo();
                content.setUser(userVo);
                content.setSeat(seatNum);
                message.setContent(content);
                messagePublishUtil.sendToRoomPublic(room.getRoomId(),message);
                countDownLatch.countDown();
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean confirmId(long id){
        if(id < messageId){
            return false;
        }
        if(id > messageId){
            log.error("出现未来包,当前id:{},收到id:{}",message,id);
            return false;
        }
        return true;
    }

    public void setMessageId(int id){
        if(messageId < id)
            this.messageId = id;
    }
}
