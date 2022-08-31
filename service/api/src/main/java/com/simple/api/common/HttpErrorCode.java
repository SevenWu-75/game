package com.simple.api.common;

/**
 * @author wangwubei
 * @description
 * @date 2021-07-1.7.21 2:27 下午
 */
public enum HttpErrorCode implements IErrorCode {

    METHOD_ARGUMENTS_TYPE_MATCH_ERROR(401,"参数格式错误"),
    MISSING_PARAMETER_ERROR(402,"缺少参数"),
    NOT_SUPPORTED_REQUEST_METHOD(403,"不支持的请求类型"),
    INVALID_REQUEST_PARAMETER(404,"不支持的请求类型");

    private int code;
    private String message;

    HttpErrorCode(int code, String message){
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
