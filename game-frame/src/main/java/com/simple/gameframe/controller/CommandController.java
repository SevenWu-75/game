package com.simple.gameframe.controller;

import com.simple.api.game.Player;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.api.game.Room;
import com.simple.api.util.ThreadLocalUtil;
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

//    @MessageMapping("/playDice")
//    public void playDice(@RequestBody Message<int[]> message){
//        Room<? extends Player> room = ThreadLocalUtil.getRoom();
//        room.getAskAnswerUtil().answerPlayDice(message);
//    }
//
//    @MessageMapping("/seatDown")
//    public void seatDown(){
//        Room room = ThreadLocalUtil.getRoom();
//        room.getAskAnswerUtil().signalSeatDown();
//    }
//
//    @MessageMapping("/selectScoreOrContinue")
//    public void selectScoreOrContinue(Message<Integer> message){
//        Room room = ThreadLocalUtil.getRoom();
//        room.getAskAnswerUtil().answerSelectScore(message);
//    }
//
//    @MessageMapping("/dismissRoom")
//    public void dismissRoom(){
//        Room room = ThreadLocalUtil.getRoom();
//        room.dismissRoom();
//        room.closeRoom();
//    }
//
//    @MessageMapping("/startGame")
//    public void startGame(Message<Void> message){
//        Room room = ThreadLocalUtil.getRoom();
//        room.getAskAnswerUtil().answerStart(message);
//    }

    @MessageMapping("/dismiss")
    public void dismissRoom(@RequestBody DefaultMessage<?> message) {
        roomHandler.dismissRoom();
    }

    @SubscribeMapping("/user/{userId}/{roomId}")
    public void subscribeRoomPrivate(@DestinationVariable("userId") String userId, @DestinationVariable("roomId") String roomId){
        //发送重连包
        MessagePublishUtil.sendMessageForReconnect(roomId, userId);
        Room<? extends Player> room = ThreadLocalUtil.getRoom();
        //通知用户重连
        EventPublisher eventPublisher = ApplicationContextUtil.getEventPublisher();
        eventPublisher.reconnect(room,userId);
        //游戏未开始且进来的玩家并未坐下则自动让他坐下
        roomHandler.signalSeatDown();
    }
}
