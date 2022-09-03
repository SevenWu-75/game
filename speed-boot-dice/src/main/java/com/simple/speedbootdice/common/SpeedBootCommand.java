package com.simple.speedbootdice.common;

import com.simple.gameframe.common.Command;

public enum SpeedBootCommand implements Command {
    ASK_DICE(3,"询问投骰子"),
    ANSWER_DICE(4,"应答投骰子"),
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