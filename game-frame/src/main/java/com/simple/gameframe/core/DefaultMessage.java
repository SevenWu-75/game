package com.simple.gameframe.core;

public class DefaultMessage<T> implements Message<T> {

    private int code;

    private Long fromId;

    private String roomId;

    private Long toId;

    private T content;

    private int id;

    private Integer seat;

    private Long reconnectUserId;

    private boolean privateMessage;

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public Long getFromId() {
        return this.fromId;
    }

    @Override
    public void setFromId(Long id) {
        this.fromId = id;
    }

    @Override
    public String getRoomId() {
        return this.roomId;
    }

    @Override
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public Long getToId() {
        return this.toId;
    }

    @Override
    public void setToId(Long id) {
        this.toId = id;
    }

    @Override
    public T getContent() {
        return this.content;
    }

    @Override
    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Integer getSeat() {
        return this.seat;
    }

    @Override
    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    @Override
    public boolean getPrivateMessage() {
        return this.privateMessage;
    }

    @Override
    public void setPrivateMessage(boolean isPrivate) {
        this.privateMessage = isPrivate;
    }

    @Override
    public Long getReconnectUserId() {
        return this.reconnectUserId;
    }

    @Override
    public void setReconnectUserId(Long userId) {
        this.reconnectUserId = userId;
    }
}
