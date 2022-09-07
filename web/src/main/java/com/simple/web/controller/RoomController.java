package com.simple.web.controller;

import com.simple.api.game.*;
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

    @DubboReference
    HistoryRankService historyRankService;

    @GetMapping("/create")
    public String createRoom(HttpSession session){
        UserVO user = ThreadLocalUtil.getUser();
        RoomVO<? extends Player> room = ThreadLocalUtil.getRoom();
        if(room != null){
            RoomVO<? extends Player> roomByRoomIdAndGameName = roomManagerService.getRoomByRoomIdAndGameName(room.getRoomId(), "");
            if(roomByRoomIdAndGameName == null || roomByRoomIdAndGameName.getRoomStatus() == 2){
                room = null;
            }
        }
        if(room == null){
            log.trace("尝试创建房间");
            room = roomManagerService.createRoomByGameName("", user);
        }
        cache(session, user, room);
        return "room";
    }

    @GetMapping("/join")
    public String joinRoom(HttpSession session, @RequestParam("id") String id){
        UserVO user = ThreadLocalUtil.getUser();
        RoomVO<? extends Player> room = ThreadLocalUtil.getRoom();
        if(room == null){
            room = roomManagerService.getRoomByRoomIdAndGameName(id, "");
            if(room == null){
                return "login";
            }
        }
        cache(session, user, room);
        return "room";
    }

    private void cache(HttpSession session, UserVO user, RoomVO<? extends Player> room) {
        HistoryRank historyRank = historyRankService.getHistoryRank(user.getId(), 1);
        HistoryRankVO historyRankVO = new HistoryRankVO(historyRank);
        user.setHistoryRankVO(historyRankVO);
        session.setAttribute("room",room);
        session.setAttribute("user",user);
        session.setAttribute("wsUrl",websocketUrl);
    }
}
