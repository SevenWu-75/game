package com.simple.api.game;

import com.simple.api.user.entity.User;

import java.io.Serializable;
import java.util.List;

public interface Player extends Serializable {

    int getId();

    User getUser();

    int getStatus();
}
