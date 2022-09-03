package com.simple.api.game;

import com.simple.api.game.entity.Game;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.api.user.entity.User;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public interface Room<T extends Player> {

    void join(User user);

    String getRoomId();

    User getOwner();

    int getRoomStatus();

    Date getCreateTime();

    Date getStartTime();

    Date getEndTime();

    List<T> getPlayerList();

    Set<User> getOnlooker();

    int getPlayCount();

    int getPlayAtLeastNum();

    int seatDown(User user);

    void start();

    void end();
}
