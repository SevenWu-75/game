package com.simple.api.game;

import com.simple.api.user.entity.User;

import java.util.*;

public interface Room extends Runnable {

    void join(User user);

    void dismissRoom();

    void closeRoom();

    String getRoomId();

    User getOwner();

    int getRoomStatus();

    Date getCreateTime();

    Date getStartTime();

    Date getEndTime();

    LinkedList<Player> getPlayerList();

    Set<User> getOnlooker();

}
