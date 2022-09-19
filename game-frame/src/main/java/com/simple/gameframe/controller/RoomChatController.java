package com.simple.gameframe.controller;

import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.util.MessagePublishUtil;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class RoomChatController {

    @MessageMapping("/room_chat")
    public void dismissRoom(@RequestBody DefaultMessage<?> message) {
        MessagePublishUtil.sendToRoomPublic(message.getRoomId(),message);
    }
}
