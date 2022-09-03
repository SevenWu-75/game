package com.simple.gameframe.core;

public class DefaultMessage<T> implements Message<T> {

    protected int code;

    protected Long fromId;

    protected String roomId;

    protected Long toId;

    protected T content;

    protected long id;

    protected Integer seat;

    protected Long reconnectUserId;

    protected boolean privateMessage;

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
    public long getId() {
        return this.id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
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
