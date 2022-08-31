package com.simple.api.common;

/**
 * @author Ancy
 * @date 2020/12/23 13:15
 * @describe
 */
public class Result<T> {
    private int code;
    private String message;
    private T data;

    private Result(){

    }

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> success(T data){
        return new Result<T>(ResultCode.SUCCESS.getCode(),ResultCode.SUCCESS.getMessage(),data);
    }

    public static <T> Result<T> success(String message,T data){
        return new Result<T>(ResultCode.SUCCESS.getCode(),message,data);
    }

    public static <T> Result<T> failed(IErrorCode resultCode){
        return new Result<T>(resultCode.getCode(),resultCode.getMessage(),null);
    }

    public static <T> Result<T> failed(){
        return new Result<T>(ResultCode.FAILED.getCode(),ResultCode.FAILED.getMessage(),null);
    }

    public static <T> Result<T> failed(IErrorCode resultCode, String message){
        return new Result<T>(ResultCode.FAILED.getCode(),message,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
