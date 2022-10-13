package com.simple.api.game;

import com.simple.api.user.entity.User;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {

    /**
     * 获取该玩家的id，作用由实际业务而定，如可以用来表示座位号
     *
     * @return 玩家的id
     */
    int getId();

    /**
     * 获取玩家的用户信息
     *
     * @return 用户信息
     */
    UserVO getUser();

    /**
     * 获取当前玩家的状态，如是否准备
     *
     * @return 玩家状态
     */
    int getStatus();
}
