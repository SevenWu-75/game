package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.common.GameCommand;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.util.ApplicationContextUtil;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Builder
public class RoomHandlerProcessor implements RoomHandler {

    private long waitTimeOutToCloseRoom = 5L;

    private RoundHandler roundHandler;

    private Room<? extends Player> room;

    private Lock lock = new ReentrantLock();

    @Override
    public Lock getLock(){
        return lock;
    }

    @Override
    public void setRoom(Room<? extends Player> room){
        this.room = room;
    }

    public void start(Room<? extends Player> room) {
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        try{
            //创建房间
            eventPublisher.create(room,null);
            CountDownLatch countDownLatch = RoomPropertyManagerUtil.getCountDownLatch(room.getRoomId(), room.getPlayCount());
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

    public void awaitStart(@NotNull CountDownLatch countDownLatch){
        try {
            boolean await = countDownLatch.await(waitTimeOutToCloseRoom, TimeUnit.MINUTES);
            if(!await){
                throw new GameException(GameExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void signalSeatDown(){
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        Room<? extends Player> room = ThreadLocalUtil.getRoom();
        User user = ThreadLocalUtil.getUser();
        RoomPropertyManagerUtil.getLock(room.getRoomId()).lock();
        try{
            if(room.getRoomStatus() == 0){
                Player player = room.seatDown(user);
                eventPublisher.seatDown(room, player);
                RoomPropertyManagerUtil.getCountDownLatch(room.getRoomId(),room.getPlayCount()).countDown();
            }
        } finally {
            RoomPropertyManagerUtil.getLock(room.getRoomId()).unlock();
        }
    }

    @Override
    public void run() {
        final Room<? extends Player> currentRoom = this.room;
        this.room = null;
        lock.unlock();
        start(currentRoom);
    }
}
