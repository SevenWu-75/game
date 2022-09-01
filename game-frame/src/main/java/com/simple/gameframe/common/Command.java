package com.simple.gameframe.common;

public enum Command {

    CREATE(100,"创建"),
    SEAT_DOWN(101,"坐下"),
    START_GAME(102,"开始游戏"),
    DISCONNECT(103,"断线"),
    CONNECT(104,"重连"),
    DISMISS_ROOM(105,"解散房间"),
    TURN_NEXT(106,"换人"),
    GAME_OVER(107,"游戏结束"),

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
