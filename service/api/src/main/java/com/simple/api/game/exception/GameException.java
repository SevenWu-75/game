package com.simple.api.game.exception;

public class GameException extends RuntimeException {

    private final GameExceptionEnum code;

    public GameExceptionEnum getCode(){
        return code;
    }

    public GameException(GameExceptionEnum code){
        this.code = code;
    }
}
