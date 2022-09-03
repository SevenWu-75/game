package com.simple.api.game;

import com.simple.api.user.entity.User;

import java.util.List;

public interface Player {

    int getId();

    User getUser();

    int getStatus();
}
