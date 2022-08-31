package com.simple.gameframe.core;

import java.util.List;

public class LogicHandlerProcessor {

    private List<LogicHandler> logicHandlerList;

    public void handle(Long userId, String roomId){
        for (LogicHandler logicHandler : logicHandlerList) {
            if (!logicHandler.handle(userId, roomId)) {
                break;
            }
        }
    }
}
