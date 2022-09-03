package com.simple.speedbootdice.pojo;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.user.entity.User;
import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.core.AbstractRoom;
import com.simple.gameframe.util.ApplicationContextUtil;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.speedbootdice.common.SpeedBootCommand;
import com.simple.speedbootdice.common.ScoreEnum;
import com.simple.speedbootdice.util.*;
import com.simple.speedbootdice.vo.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
