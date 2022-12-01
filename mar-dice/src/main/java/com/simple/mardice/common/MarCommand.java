package com.simple.mardice.common;

import com.simple.gameframe.common.Command;

public enum MarCommand implements Command {

    PLAY_DICE(1,"抛掷骰子"),
    PLAY_DICE_RESULT(2,"骰子结果"),
    END_ROUND(3,"结束回合"),
    SELECT_DICE(4, "选择骰子"),
    SELECT_OR_END(5,"询问结束回合或选择骰子"),
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