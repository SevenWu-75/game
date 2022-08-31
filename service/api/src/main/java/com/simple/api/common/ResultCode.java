package com.simple.api.common;

/**
 * @author Ancy
 * @date 2020/12/23 13:16
 * @description
 */
public enum ResultCode implements IErrorCode{
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    UNAUTHORIZED(501, "暂未登录或token已经过期"),
    VALIDATE_FAILED(502, "参数检验失败"),
    FORBIDDEN(503, "没有相关权限"),
    DATABASE_ERROR(505,"数据库错误"),
    LOGIN_FAILED(506,"账号或密码错误"),
    ACCOUNT_EXIST(507,"账号已存在"),
    ;

    private int code;
    private String message;

    ResultCode(int code,String message) {
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
