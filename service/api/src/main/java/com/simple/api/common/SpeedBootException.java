package com.simple.api.common;

public class SpeedBootException extends RuntimeException {

    private final SpeedBootExceptionEnum code;

    public SpeedBootExceptionEnum getCode(){
        return code;
    }

    public SpeedBootException(SpeedBootExceptionEnum code){
        this.code = code;
    }
}
