package com.simple.gameservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.api.game.entity.Game;
import com.simple.api.game.service.GameService;
import com.simple.gameservice.mapper.GameMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

@DubboService
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {

    @Override
    public Game getGameByName(String name) {
        LambdaQueryWrapper<Game> gameLambdaQueryWrapper = new LambdaQueryWrapper<>();
        gameLambdaQueryWrapper.eq(Game::getGameName, name);
        return this.baseMapper.selectOne(gameLambdaQueryWrapper);
    }
}
