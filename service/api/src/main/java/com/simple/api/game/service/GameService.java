package com.simple.api.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.api.game.entity.Game;

public interface GameService extends IService<Game> {

    Game getGameByName(String name);
}
