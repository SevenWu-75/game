package com.simple.api.game;

import java.io.Serializable;
import java.util.*;

public interface Room<T extends Player> extends Serializable {

    /**
     * 获取当前房间的房间号
     *
     * @return 房间号
     */
    String getRoomId();

    /**
     * 获取当前房间的所有者
     *
     * @return 所有者用户信息
     */
    UserVO getOwner();

    /**
     * 获取当前房间状态
     *
     * @see RoomStatusEnum
     * @return 房间状态
     */
    int getRoomStatus();

    /**
     * 获取房间的创建时间
     *
     * @return 创建时间
     */
    Date getCreateTime();

    /**
     * 获取房间的开始游戏时间
     *
     * @return 开始时间
     */
    Date getStartTime();

    /**
     * 获取房间的结束时间
     *
     * @return 结束时间
     */
    Date getEndTime();

    /**
     * 获取房间的玩家列表
     *
     * @return 玩家列表
     */
    List<T> getPlayerList();

    /**
     * 获取房间的旁观者用户集合
     *
     * @return 旁观者用户集合
     */
    Set<UserVO> getOnlooker();

    /**
     * 获取游戏的总回合数
     *
     * @return 回合数
     */
    int getPlayCount();

    /**
     * 获取游戏开始所需最少的玩家数量
     *
     * @return 最少玩家数量
     */
    int getPlayAtLeastNum();

    /**
     * 获取当前正在回合的座位号
     * 注：主要用于做一个当前房间状态的快照，重连时可以进行加载
     *
     * @return 正在回合的座位号
     */
    int getCurrentSeat();

    /**
     * 更新当前座位号
     *
     * @param player 当前正在执行回合的玩家id
     */
    void setCurrentSeat(int player);

    /**
     * 获取本局游戏生成过最大的消息包id
     * 注：主要用于做一个当前房间状态的快照，重连时可以进行加载
     *
     * @return 最大的消息包id
     */
    long getMaxMessageId();

    /**
     * 更新本局消息包id记录
     *
     * @param messageId 消息包id
     */
    void setMaxMessageId(long messageId);
}
