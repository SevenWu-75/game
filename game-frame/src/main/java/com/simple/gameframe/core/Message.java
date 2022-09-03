package com.simple.gameframe.core;

public interface Message<T> {

    int getCode();

    void setCode(int code);

    Long getFromId();

    void setFromId(Long id);

    String getRoomId();

    void setRoomId(String roomId);

    Long getToId();

    void setToId(Long id);

    T getContent();

    void setContent(T content);

    long getId();

    void setId(long id);

    Integer getSeat();

    void setSeat(Integer seat);

    boolean getPrivateMessage();

    void setPrivateMessage(boolean isPrivate);

    Long getReconnectUserId();

    void setReconnectUserId(Long userId);
}
