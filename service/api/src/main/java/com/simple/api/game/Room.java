package com.simple.api.game;

import com.simple.api.game.entity.Game;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.api.user.entity.User;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public interface Room<T extends Player> extends Serializable {

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
}
