package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.common.Command;
import com.simple.gameframe.common.GameCommand;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.gameframe.core.ask.LogicHandler;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.util.ApplicationContextUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
public class RoomHandlerProcessor implements RoomHandler {

    private final long waitTimeOutToCloseRoom = 5L;

    private final RoundHandler roundHandler;

    private Room<? extends Player> room;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private final LogicHandler<? extends Command> startLogicHandler;

    public RoomHandlerProcessor(RoundHandler roundHandler, LogicHandler<? extends Command> startLogicHandler) {
        this.roundHandler = roundHandler;
        this.startLogicHandler = startLogicHandler;
    }

    @Override
    public void setRoom(Room<? extends Player> room){
        lock.writeLock();
        this.room = room;
    }

    public void start(Room<? extends Player> room) {
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        try{
            //创建房间
            eventPublisher.create(room, null,null);
            CountDownLatch countDownLatch = RoomPropertyManagerUtil.getCountDownLatch(room.getRoomId(), room.getPlayAtLeastNum());
            awaitStart(countDownLatch);
            //可以开始,询问开始游戏
            eventPublisher.canStart(room, null,null);
            List<? extends Player> playerList = room.getPlayerList();
            preHandle(playerList.get(0),room, RoomPropertyManagerUtil.getLock(room.getRoomId()));
            //开始游戏
            eventPublisher.start(room, null,null);
            //开始回合
            Object roundResult = roundHandler.startLogic(room);
            Thread.sleep(500);
            //游戏结果
            eventPublisher.gameResult(room, null, roundResult);
            Thread.sleep(500);
            //游戏结束
            eventPublisher.gameOver(room,null,null);
        } catch (GameException e) {
            if (e.getCode().equals(GameExceptionEnum.CLOSE_FOR_OPERATE_TIMEOUT)) {
                eventPublisher.timeout(room, null,null);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void preHandle(Player player, Room<? extends Player> room, Lock lock){
        if(startLogicHandler != null){
            startLogicHandler.preHandle(player, room, null);
            Message<?> sendMessage = startLogicHandler.messageHandle(player, room, null);
            Message<?> receiveMessage = startLogicHandler.ask(player.getUser().getId(), room, sendMessage, lock,
                    RoomPropertyManagerUtil.getCondition(room.getRoomId(), startLogicHandler.toString()));
            startLogicHandler.postHandle(player, room, receiveMessage, null);
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
        AbstractRoom<? extends Player> room = (AbstractRoom<? extends Player>)RoomPropertyManagerUtil.getRoomImpl(ThreadLocalUtil.getRoom().getRoomId());
        UserVO user = ThreadLocalUtil.getUser();
        RoomPropertyManagerUtil.getLock(room.getRoomId()).lock();
        try{
            if(room.getRoomStatus() == 0){
                Player player = room.seatDown(user);
                eventPublisher.seatDown(room, player, player);
                RoomPropertyManagerUtil.getCountDownLatch(room.getRoomId(),room.getPlayAtLeastNum()).countDown();
            }
        } finally {
            RoomPropertyManagerUtil.getLock(room.getRoomId()).unlock();
        }
    }

    @Override
    public void dismissRoom(){
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        AbstractRoom<? extends Player> room = (AbstractRoom<? extends Player>)RoomPropertyManagerUtil.getRoomImpl(ThreadLocalUtil.getRoom().getRoomId());
        UserVO user = ThreadLocalUtil.getUser();
        Optional<? extends Player> first = room.getPlayerList().stream().filter(player -> player.getUser().getId().equals(user.getId())).findFirst();
        eventPublisher.dismiss(room, first.orElse(null), null);
    }

    @Override
    public void run() {
        final Room<? extends Player> currentRoom = this.room;
        this.room = null;
        lock.writeLock();
        log.trace("开始房间逻辑");
        start(currentRoom);
    }
}
