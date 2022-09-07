package com.simple.speedbootdice.common;

import com.simple.gameframe.common.Command;

public enum SpeedBootCommand implements Command {
    PLAY_DICE(3,"抛掷骰子"),
    DICE_RESULT(4,"骰子结果"),
    SELECT_SCORE(5,"选择分数"),
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