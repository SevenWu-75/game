package com.simple.gameframe.common;

public enum Command {

    CREATE(100,"创建"),
    JOIN(101,"加入"),
    CAN_START(102,"可以开始游戏"),
    START_GAME(103,"开始游戏"),
    TURN_NEXT(104,"换人"),
    TURN_ROUND(105,"换圈"),
    DISCONNECT(106,"断线"),
    RECONNECT(107,"重连"),
    DISMISS_ROOM(108,"解散房间"),
    VOTE_DISMISS(109,"投票解散"),
    GAME_OVER(110,"游戏结束"),
    ;

    private int code;
    private String message;

    Command(int code,String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
