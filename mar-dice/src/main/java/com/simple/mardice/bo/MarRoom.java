package com.simple.mardice.bo;

import com.simple.api.game.UserVO;
import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.core.AbstractRoom;
import com.simple.gameframe.core.ClassInject;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@ClassInject("com.simple.api.game.Room")
@Slf4j
public class MarRoom extends AbstractRoom<MarPlayer> implements Serializable {

    private int playCount;

    private final int playerAtLeastNum;

    public MarRoom(UserVO user, GameFrameProperty gameFrameProperty) {
        super(user, gameFrameProperty);
        //先将游戏回合数设置最大，当有玩家分数到达25分及以上则设为0就能结束游戏
        this.playCount = 99;
        this.playerAtLeastNum = 2;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    @Override
    public int getPlayCount() {
        return playCount;
    }

    @Override
    public int getPlayAtLeastNum() {
        return playerAtLeastNum;
    }
}
