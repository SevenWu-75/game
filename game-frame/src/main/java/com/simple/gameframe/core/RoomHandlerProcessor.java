package com.simple.gameframe.core;

import com.simple.api.game.Room;
import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.common.Command;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.gameframe.core.ask.AskAnswerLockConditionManager;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.core.publisher.RoomEventPublisher;
import com.simple.gameframe.util.ApplicationContextUtil;
import lombok.Builder;

import javax.ws.rs.core.Application;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Builder
public class RoomHandlerProcessor implements RoomHandler {

    private long waitTimeOutToCloseRoom = 5L;

    private RoundHandler roundHandler;

    private Room room;

    private Lock lock = new ReentrantLock();

    @Override
    public Lock getLock(){
        return lock;
    }

    @Override
    public void setRoom(Room room){
        this.room = room;
    }

    public void start(Room room) {
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        try{
            //创建房间
            eventPublisher.create(room,null);
            CountDownLatch countDownLatch = AskAnswerLockConditionManager.getCountDownLatch(room.getRoomId(), room.getPlayCount());
            awaitStart(countDownLatch);
            //可以开始
            eventPublisher.canStart(room, null);
            if(roundHandler == null){
                throw new GameException(GameExceptionEnum.FAILED);
            }
            roundHandler.startLogic(room);
        } catch (GameException e) {
            if (e.getCode().equals(GameExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT)) {
                eventPublisher.timeout(room, null);
            }
        }

    }

    public void awaitStart(CountDownLatch countDownLatch){
        try {
            boolean await = countDownLatch.await(waitTimeOutToCloseRoom, TimeUnit.MINUTES);
            if(!await){
                throw new GameException(GameExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void signalSeatDown(){
        Room room = ThreadLocalUtil.getRoom();
        User userVo = ThreadLocalUtil.getUser();
        AskAnswerLockConditionManager.getLock(room.getRoomId()).lock();
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
                AskAnswerLockConditionManager.getCountDownLatch(room.getRoomId(),room.getPlayCount()).countDown();
            }
        } finally {
            AskAnswerLockConditionManager.getLock(room.getRoomId()).unlock();
        }
    }

    @Override
    public void run() {
        final Room currentRoom = this.room;
        this.room = null;
        lock.unlock();
        start(currentRoom);
    }
}
