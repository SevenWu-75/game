package com.simple.speedbootdice.common;

import com.simple.gameframe.common.Command;

public enum SpeedBootCommand implements Command {
    PLAY_DICE(3,"抛掷骰子"),
    DICE_RESULT(4,"骰子结果"),
    PLAY_DICE_OR_SELECT_SCORE(6, "抛掷骰子还是选择分数"),
    SELECT_SCORE(5,"选择分数"),
    LOCK_DICE(7,"锁骰子"),
    ;

    private final int code;
    private final String message;

    SpeedBootCommand(int code,String message) {
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