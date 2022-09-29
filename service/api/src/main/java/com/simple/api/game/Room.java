package com.simple.api.game;

import java.io.Serializable;
import java.util.*;

public interface Room<T extends Player> extends Serializable {

    String getRoomId();

    UserVO getOwner();

    int getRoomStatus();

    Date getCreateTime();

    Date getStartTime();

    Date getEndTime();

    List<T> getPlayerList();

    Set<UserVO> getOnlooker();

    int getPlayCount();

    int getPlayAtLeastNum();

    int getCurrentSeat();

    void setCurrentSeat(int player);

    long getMaxMessageId();

    void setMaxMessageId(long messageId);
}
