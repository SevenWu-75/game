package com.simple.gameframe.common;

public enum GameCommand implements Command {

    CREATE(100,"创建"),
    JOIN(101,"加入"),
    SEAT_DOWN(102,"坐下"),
    CAN_START(103,"可以开始游戏"),
    START_GAME(104,"开始游戏"),
    TURN_NEXT(105,"换人"),
    TURN_ROUND(106,"换圈"),
    DISCONNECT(107,"断线"),
    RECONNECT(108,"重连"),
    DISMISS_ROOM(109,"解散房间"),
    VOTE_DISMISS(110,"投票解散"),
    GAME_RESULT(111,"游戏结果"),
    GAME_OVER(112,"游戏结束"),
    TIMEOUT(113,"超时结束"),
    ;

    private int code;
    private String message;

    GameCommand(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
