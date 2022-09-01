package com.simple.api.game;

import com.simple.api.user.entity.User;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public interface Room {

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

    int getPlayCount();

    int getPlayAtLeastNum();
}
