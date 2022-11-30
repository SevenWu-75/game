package com.simple.mardice.common;

import com.simple.gameframe.common.Command;

public enum MarCommand implements Command {

    PLAY_DICE(1,"抛掷骰子"),
    END_ROUND(2,"结束回合"),
    SELECT_DICE(3, "选择骰子"),
    SELECT_OR_END(4,"询问结束回合或选择骰子"),
    ;

    private final int code;
    private final String message;

    MarCommand(int code,String message) {
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