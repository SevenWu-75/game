package com.simple.web.controller;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.api.game.entity.HistoryRank;
import com.simple.api.game.service.RoomManagerService;
import com.simple.api.user.entity.User;
import com.simple.api.game.service.HistoryRankService;
import com.simple.api.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/room")
@Slf4j
public class RoomController {

    @DubboReference
    RoomManagerService roomManagerService;

    @Value("${websocket.url}")
    String websocketUrl;

    @GetMapping("/create")
    public String createRoom(HttpSession session){
        User user = ThreadLocalUtil.getUser();
        RoomVO<? extends Player> room = ThreadLocalUtil.getRoom();
        if(room == null){
            log.trace("尝试创建房间");
            room = roomManagerService.createRoomByGameName("", user);
        }
        session.setAttribute("room", room);
        session.setAttribute("user",user);
        session.setAttribute("wsUrl",websocketUrl);
        return "room";
    }

    @GetMapping("/join")
    public String joinRoom(HttpSession session, @RequestParam("id") String id){
        User user = ThreadLocalUtil.getUser();
        RoomVO<? extends Player> room = ThreadLocalUtil.getRoom();
        if(room == null){
            room = roomManagerService.createRoomByGameName("", user);
            if(room == null){
                return "login";
            }
        }
        session.setAttribute("room",room);
        session.setAttribute("user",user);
        session.setAttribute("wsUrl",websocketUrl);
        return "room";
    }

}
