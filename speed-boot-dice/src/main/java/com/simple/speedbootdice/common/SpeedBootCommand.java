package com.simple.speedbootdice.common;

public enum SpeedBootCommand {
    ASK_DICE(3,"询问投骰子"),
    ANSWER_DICE(4,"应答投骰子"),
    SELECT_SCORE(5,"选择分数"),
    ;

    private int code;
    private String message;

    SpeedBootCommand(int code,String message) {
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