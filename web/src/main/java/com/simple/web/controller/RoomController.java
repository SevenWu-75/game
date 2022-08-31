package com.simple.web.controller;

import com.simple.api.game.Room;
import com.simple.api.game.entity.HistoryRank;
import com.simple.api.game.service.RoomManagerService;
import com.simple.api.user.entity.User;
import com.simple.api.game.service.HistoryRankService;
import com.simple.api.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
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
    HistoryRankService historyRankService;

    @DubboReference
    RoomManagerService roomManagerService;

    @Value("${websocket.url}")
    String websocketUrl;

    @GetMapping("/create")
    public String createRoom(HttpSession session){
        User user = ThreadLocalUtil.getUser();
        Room room = ThreadLocalUtil.getRoom();
        if(room == null){
            room = roomManagerService.createRoomByGameName("", user);
            HistoryRank historyRank = historyRankService.getHistoryRank(user.getId(), 1);
            user.setHistoryRank(historyRank == null ? new HistoryRank() : historyRank);
        }
        session.setAttribute("room", room);
        session.setAttribute("user",user);
        session.setAttribute("wsUrl",websocketUrl);
        return "room";
    }

    @GetMapping("/join")
    public String joinRoom(HttpSession session, @RequestParam("id") String id){
        User user = ThreadLocalUtil.getUser();
        Room room = ThreadLocalUtil.getRoom();
        if(room == null){
            room = roomManagerService.createRoomByGameName("", user);
            if(room == null){
                return "login";
            }
            room.join(user);
            HistoryRank historyRank = historyRankService.getHistoryRank(user.getId(), 1);
            user.setHistoryRank(historyRank == null ? new HistoryRank() : historyRank);
        }
        session.setAttribute("room",room);
        session.setAttribute("user",user);
        session.setAttribute("wsUrl",websocketUrl);
        return "room";
    }

}
