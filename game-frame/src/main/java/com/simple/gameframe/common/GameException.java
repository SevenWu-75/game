package com.simple.gameframe.common;

public class GameException extends RuntimeException {

    private final GameExceptionEnum code;

    public GameExceptionEnum getCode(){
        return code;
    }

    public GameException(GameExceptionEnum code){
        this.code = code;
    }
}
