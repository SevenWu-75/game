package com.simple.gameframe.core;

import com.simple.api.game.Message;

public class DefaultMessage<T> implements Message<T> {

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public void setCode(int code) {

    }

    @Override
    public Long getFromId() {
        return null;
    }

    @Override
    public void setFromId(Long id) {

    }

    @Override
    public String getRoomId() {
        return null;
    }

    @Override
    public void setRoomId(String roomId) {

    }

    @Override
    public Long getToId() {
        return null;
    }

    @Override
    public void setToId(Long id) {

    }

    @Override
    public T getContent() {
        return null;
    }

    @Override
    public void setContent(T content) {

    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public Integer getSeat() {
        return null;
    }

    @Override
    public void setSeat(Integer seat) {

    }

    @Override
    public boolean getPrivateMessage() {
        return false;
    }

    @Override
    public void setPrivateMessage(boolean isPrivate) {

    }

    @Override
    public Long getReconnectUserId() {
        return null;
    }

    @Override
    public void setReconnectUserId(Long userId) {

    }
}
