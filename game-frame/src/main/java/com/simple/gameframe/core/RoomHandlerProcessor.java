package com.simple.gameframe.core;

import com.simple.api.game.Room;
import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.common.GameException;
import com.simple.gameframe.common.GameExceptionEnum;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RoomHandlerProcessor implements RoomHandler {

    private ConcurrentHashMap<String, CountDownLatch> countDownMap = new ConcurrentHashMap<>();

    private long waitTimeOutToCloseRoom = 5L;

    @Override
    public CountDownLatch getCountDownLatch(String roomId, int minPlayer) {
        if(countDownMap.containsKey(roomId))
            return countDownMap.get(roomId);
        CountDownLatch countDownLatch = new CountDownLatch(minPlayer);
        countDownMap.put(roomId, countDownLatch);
        return countDownLatch;
    }

    @Override
    public void cleanCountDownLatch(String roomId) {
        countDownMap.remove(roomId);
    }

    public void start(Room room){
        CountDownLatch countDownLatch = getCountDownLatch(room.getRoomId(), room.getPlayCount());
        awaitStart(countDownLatch);
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
}
