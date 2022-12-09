package com.simple.web.controller;

import com.simple.api.game.*;
import com.simple.api.game.entity.HistoryRank;
import com.simple.api.game.service.RoomManagerService;
import com.simple.api.game.service.HistoryRankService;
import com.simple.api.util.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/room")
@Slf4j
public class RoomController {

    @DubboReference
    RoomManagerService roomManagerService;

    @Value("${websocket.speed-boot}")
    String speedBootWebsocketUrl;

    @Value("${websocket.mar}")
    String marWebsocketUrl;

    @DubboReference
    HistoryRankService historyRankService;

    @GetMapping("/create")
    public String createRoom(HttpSession session, @RequestParam("gameId") String gameId){
        UserVO user = ThreadLocalUtil.getUserVO();
        RoomVO<? extends Player> room = ThreadLocalUtil.getRoomVO();
        if(room != null){
            room = roomManagerService.getRoomByRoomIdAndGameName(room.getRoomId(), gameId);
        }
        if(room == null || room.getRoomStatus() == RoomStatusEnum.over.ordinal()){
            log.trace("尝试创建房间");
            room = roomManagerService.createRoomByGameName(gameId, user);
        }
        return cache(session, user, room);
    }

    @GetMapping("/join")
    public String joinRoom(HttpSession session, @RequestParam("id") String id){
        UserVO user = ThreadLocalUtil.getUserVO();
        RoomVO<? extends Player> room = ThreadLocalUtil.getRoomVO();
        if(room != null){
            room = roomManagerService.getRoomByRoomId(room.getRoomId());
        }
        if(room == null || room.getRoomStatus() == RoomStatusEnum.over.ordinal()){
            room = roomManagerService.getRoomByRoomId(id);
            if(room == null){
                return "login";
            }
        }
        return cache(session, user, room);
    }

    private String cache(HttpSession session, UserVO user, RoomVO<? extends Player> room) {
        HistoryRank historyRank = historyRankService.getHistoryRank(user.getId(), Integer.parseInt(room.getGameName()));
        HistoryRankVO historyRankVO = new HistoryRankVO();
        if(historyRank != null){
            historyRankVO = new HistoryRankVO(historyRank);
        }
        user.setHistoryRankVO(historyRankVO);
        session.setAttribute("room",room);
        session.setAttribute("user",user);
        if(room.getGameName().equals("2")) {
            session.setAttribute("wsUrl",marWebsocketUrl);
            return "mar";
        }
        session.setAttribute("wsUrl",speedBootWebsocketUrl);
        return "room";
    }
}
