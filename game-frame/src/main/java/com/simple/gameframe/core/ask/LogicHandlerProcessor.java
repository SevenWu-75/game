package com.simple.gameframe.core.ask;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.gameframe.common.GameException;
import com.simple.gameframe.core.Message;
import lombok.Builder;

import java.util.List;
import java.util.concurrent.locks.Lock;

@Builder
public class LogicHandlerProcessor {

    private List<LogicHandler> logicHandlerList;

    private LogicHandler waitStartLogicHandler;

    public LogicHandlerProcessor(List<LogicHandler> logicHandlerList){
        this.logicHandlerList = logicHandlerList;
    }

    public Object process(Player player, Room room, Lock lock){
        preHandle(player, room, lock);
        return handle(player, room, lock);
    }

    private void preHandle(Player player, Room room, Lock lock){
        if(waitStartLogicHandler != null){
            waitStartLogicHandler.preHandle(player, room, null);
            try{
                Message<?> sendMessage = waitStartLogicHandler.messageHandle(player, room, null);
                Message<?> receiveMessage = waitStartLogicHandler.ask(player.getUser().getId(), room, sendMessage, lock,
                        AskAnswerLockConditionManager.getCondition(room.getRoomId(), waitStartLogicHandler.toString()));
                waitStartLogicHandler.postHandle(player, room, receiveMessage, null);
            } catch (GameException e){

            }
        }
    }

    private Object handle(Player player, Room room, Lock lock){
        Object o = null;
        if (logicHandlerList.size() > 0) {
            //灵活设置下一个处理器，链式执行的方式
            LogicHandler firstLogicHandler = logicHandlerList.get(0);
            LogicHandler nextHandler = firstLogicHandler.getNextHandler();
            if(nextHandler != null){
                while(nextHandler != null){
                    //如果不满足执行条件则跳过本次询问
                    if(!nextHandler.preHandle(player, room, o)){
                        continue;
                    }
                    //如果执行结果为false则不继续执行接下来的所有询问
                    try {
                        Message<?> sendMessage = nextHandler.messageHandle(player, room, o);
                        Message<?> receiveMessage = nextHandler.ask(player.getUser().getId(), room, sendMessage, lock,
                                AskAnswerLockConditionManager.getCondition(room.getRoomId(), nextHandler.toString()));
                        o = nextHandler.postHandle(player, room, receiveMessage, o);
                    } catch (GameException e){
                        break;
                    }
                    nextHandler = nextHandler.getNextHandler();
                }
            } else {
                //自动循环执行的方式
                for (LogicHandler logicHandler : logicHandlerList) {
                    //如果不满足执行条件则跳过本次询问
                    if(!logicHandler.preHandle(player, room, o)){
                        continue;
                    }
                    //如果执行结果为false则不继续执行接下来的所有询问
                    try {
                        Message<?> sendMessage = logicHandler.messageHandle(player, room, o);
                        Message<?> receiveMessage = logicHandler.ask(player.getUser().getId(), room, sendMessage, lock,
                                AskAnswerLockConditionManager.getCondition(room.getRoomId(), logicHandler.toString()));
                        o = logicHandler.postHandle(player, room, receiveMessage, o);
                    } catch (GameException e){
                        break;
                    }
                }
            }
        }
        return o;
    }
}
