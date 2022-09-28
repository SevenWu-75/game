package com.simple.gameframe.core;

import com.simple.api.game.Player;

import java.util.Collections;
import java.util.List;

public interface SeatHandler {

    /**
     * 打乱玩家次序
     *
     * @param players 玩家列表
     * @return 返回乱序玩家列表
     */
    default List<Player> randomSeat(List<Player> players){
        if(players.size() > 1){
            Collections.shuffle(players);
        }
        return players;
    }

    /**
     * 根据回合数和上轮玩家次序获取本轮玩家次序(默认实现和上轮次序一样)
     *
     * @param round 回合数
     * @param lastPlayers 上轮玩家次序
     * @param o 上轮的结果
     * @return 返回本轮玩家次序列表
     */
    default List<Player> getRoundPlayer(int round, List<Player> lastPlayers, Object o) {
        return lastPlayers;
    }
}
