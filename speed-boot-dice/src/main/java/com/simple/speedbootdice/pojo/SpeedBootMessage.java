package com.simple.speedbootdice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simple.gameframe.core.Message;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@Slf4j
@ToString
public class SpeedBootMessage<T> implements Message<T> {

    /**
     * 消息编码
     */
    private int code;

    private Long fromId;

    private String roomId;

    /**
     * 去自（保证唯一）
     */
    private Long toId;

    /**
     * 内容
     */
    private T content;

    private int id;

    private int seat;

    @JsonIgnore
    private final static AtomicInteger commonId = new AtomicInteger(0);

    private Boolean privateMessage;

    private Long reconnectUserId;

    public SpeedBootMessage(){
        this.id = commonId.addAndGet(1);
        log.trace("生成message,id{}",id);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeedBootMessage)) return false;
        SpeedBootMessage<?> message = (SpeedBootMessage<?>) o;
        return id == message.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    @Override
    public void setPrivateMessage(boolean isPrivate) {
        this.privateMessage = isPrivate;
    }
}
