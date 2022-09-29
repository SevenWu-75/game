package com.simple.gameframe.controller;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.core.RoomHandler;
import com.simple.gameframe.core.publisher.EventPublisher;
import com.simple.gameframe.util.ApplicationContextUtil;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import com.simple.gameframe.core.ask.LogicHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class CommandController {

    @Autowired
    List<LogicHandler<?>> logicHandlerList;

    @Autowired
    RoomHandler roomHandler;

    @MessageMapping("/command")
    public void command(@RequestBody DefaultMessage<?> message){
        Room<? extends Player> room = ThreadLocalUtil.getRoom();
        logicHandlerList.forEach(logicHandler -> {
            if(logicHandler.getCommands().stream().anyMatch(command -> command.getCode() == message.getCode())){
                logicHandler.answer(RoomPropertyManagerUtil.getLock(room.getRoomId()),
                        room,
                        RoomPropertyManagerUtil.getCondition(room.getRoomId(), logicHandler.toString()),
                        message);
            }
        });
    }

    @MessageMapping("/dismiss")
    public void dismissRoom(@RequestBody DefaultMessage<?> message) {
        roomHandler.dismissRoom();
    }

    @SubscribeMapping("/user/{userId}/{roomId}")
    public void subscribeRoomPrivate(@DestinationVariable("userId") String userId, @DestinationVariable("roomId") String roomId){
        //发送重连包
        Room<? extends Player> room = ThreadLocalUtil.getRoom();
        Message<RoomVO<? extends Player>> message = new DefaultMessage<>();
        message.setRoomId(roomId);
        message.setCode(GameCommand.RECONNECT.getCode());
        message.setContent(new RoomVO<>(room));
        message.setToId(Long.parseLong(userId));
        MessagePublishUtil.sendToRoomUser(userId, roomId, message);
        //通知用户重连
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        Optional<? extends Player> first = room.getPlayerList().stream().filter(player -> userId.equals(String.valueOf(player.getUser().getId()))).findFirst();
        if(first.isPresent()){
            eventPublisher.reconnect(room, first.get(), userId);
        }
        //游戏未开始且进来的玩家并未坐下则自动让他坐下
        roomHandler.signalSeatDown();
    }

    @MessageMapping("/public")
    public void sendPublic(@RequestBody DefaultMessage<?> message){
        Room<? extends Player> room = ThreadLocalUtil.getRoom();
        MessagePublishUtil.sendToRoomPublic(room.getRoomId(),message);
    }
}
