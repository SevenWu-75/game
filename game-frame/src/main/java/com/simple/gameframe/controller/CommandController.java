package com.simple.gameframe.controller;

import com.simple.api.game.Message;
import com.simple.api.game.Room;
import com.simple.api.util.ThreadLocalUtil;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class CommandController {

    @MessageMapping("/playDice")
    public void playDice(@RequestBody Message<int[]> message){
        Room room = ThreadLocalUtil.getRoom();
        room.getAskAnswerUtil().answerPlayDice(message);
    }

    @MessageMapping("/seatDown")
    public void seatDown(){
        Room room = ThreadLocalUtil.getRoom();
        room.getAskAnswerUtil().signalSeatDown();
    }

    @MessageMapping("/selectScoreOrContinue")
    public void selectScoreOrContinue(Message<Integer> message){
        Room room = ThreadLocalUtil.getRoom();
        room.getAskAnswerUtil().answerSelectScore(message);
    }

    @MessageMapping("/dismissRoom")
    public void dismissRoom(){
        Room room = ThreadLocalUtil.getRoom();
        room.dismissRoom();
        room.closeRoom();
    }

    @MessageMapping("/startGame")
    public void startGame(Message<Void> message){
        Room room = ThreadLocalUtil.getRoom();
        room.getAskAnswerUtil().answerStart(message);
    }

    @SubscribeMapping("/user/{userId}/{roomId}")
    public void subscribeRoomPrivate(@DestinationVariable("userId") String userId, @DestinationVariable("roomId") String roomId){
        //发送重连包
        messagePublishUtil.sendMessageForReconnect(roomId, userId);
        com.simple.api.game.Room room = ThreadLocalUtil.getRoom();
        //先发送在线状态包
        room.sendPublicConnectMessage(Long.parseLong(userId));
        //游戏未开始且进来的玩家并未坐下则自动让他坐下
        room.getAskAnswerUtil().signalSeatDown();
    }
}
