package com.simple.speedbootdice.pojo;

import com.simple.api.game.UserVO;
import com.simple.api.user.entity.User;
import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.core.AbstractRoom;
import com.simple.gameframe.core.ClassInject;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@ClassInject("com.simple.api.game.Room")
@Slf4j
public class SpeedBootRoom extends AbstractRoom<SpeedBootPlayer> implements Serializable {

    public SpeedBootRoom(UserVO user, GameFrameProperty gameFrameProperty) {
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
