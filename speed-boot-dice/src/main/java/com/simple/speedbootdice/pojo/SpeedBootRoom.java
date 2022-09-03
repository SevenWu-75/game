package com.simple.speedbootdice.pojo;

import com.simple.api.user.entity.User;
import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.core.AbstractRoom;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
public class SpeedBootRoom extends AbstractRoom<SpeedBootPlayer> implements Serializable {

    public SpeedBootRoom(User user, GameFrameProperty gameFrameProperty) {
        super(user, gameFrameProperty);
        this.playCount = 13;
        this.playAtLeastNum = 2;
    }

    @Override
    public int getPlayCount() {
        return this.playCount;
    }

    @Override
    public int getPlayAtLeastNum() {
        return this.playAtLeastNum;
    }
}
