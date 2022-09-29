package com.simple.gameframe.core.ask;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.util.ApplicationContextUtil;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import lombok.Builder;

import java.util.List;
import java.util.concurrent.locks.Lock;

public class LogicHandlerProcessor {

    private final List<LogicHandler<?>> logicHandlerList;

    public LogicHandlerProcessor(List<LogicHandler<?>> logicHandlerList){
        this.logicHandlerList = logicHandlerList;
    }

    public Object handle(Player player, Room<? extends Player> room, Lock lock){
        Object o = null;
        if (logicHandlerList.size() > 0) {
            //灵活设置下一个处理器，链式执行的方式
            LogicHandler<?> firstLogicHandler = logicHandlerList.get(0);
            if(ApplicationContextUtil.getGameFrameProperty().getEnableLinkLogic()){
                LogicHandler<?> nextHandler = firstLogicHandler;
                while(nextHandler != null){
                    //如果不满足执行条件则跳过本次询问
                    if(!nextHandler.preHandle(player, room, o)){
                        continue;
                    }
                    //如果执行结果为false则不继续执行接下来的所有询问
                    Message<?> sendMessage = nextHandler.messageHandle(player, room, o);
                    Message<?> receiveMessage = nextHandler.ask(player.getUser().getId(), room, sendMessage, lock,
                            RoomPropertyManagerUtil.getCondition(room.getRoomId(), nextHandler.toString()));
                    o = nextHandler.postHandle(player, room, receiveMessage, o);
                    nextHandler = nextHandler.getNextHandler();
                }
            } else {
                //自动循环执行的方式
                for (LogicHandler<?> logicHandler : logicHandlerList) {
                    //如果不满足执行条件则跳过本次询问
                    if(!logicHandler.preHandle(player, room, o)){
                        continue;
                    }
                    //如果执行结果为false则不继续执行接下来的所有询问
                    Message<?> sendMessage = logicHandler.messageHandle(player, room, o);
                    Message<?> receiveMessage = logicHandler.ask(player.getUser().getId(), room, sendMessage, lock,
                            RoomPropertyManagerUtil.getCondition(room.getRoomId(), logicHandler.toString()));
                    o = logicHandler.postHandle(player, room, receiveMessage, o);
                }
            }
        }
        return o;
    }
}
