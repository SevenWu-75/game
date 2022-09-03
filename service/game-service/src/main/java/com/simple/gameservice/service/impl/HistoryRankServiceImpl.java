package com.simple.gameservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.simple.api.game.entity.HistoryRank;
import com.simple.api.game.service.HistoryRankService;
import com.simple.gameservice.mapper.HistoryRankMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import java.util.Date;

@DubboService
@Service
public class HistoryRankServiceImpl extends ServiceImpl<HistoryRankMapper, HistoryRank> implements HistoryRankService {

    @Override
    public void saveHistory(long userId, int gameId, boolean isWinner, long score){
        HistoryRank one = getHistoryRank(userId, gameId);
        if(one == null){
            one = new HistoryRank();
            one.setUserId(userId);
            one.setGameId(gameId);
            one.setPlayCount(1L);
            one.setWinCount(isWinner ? 1 : 0);
            one.setBestScore(score);
        } else {
            one.setPlayCount(one.getPlayCount() + 1);
            if(isWinner)
                one.setWinCount(one.getWinCount() + 1);
            if(one.getBestScore() < score)
                one.setBestScore(score);
        }
        one.setLastPlayTime(new Date());
        this.saveOrUpdate(one);
    }

    @Override
    public HistoryRank getHistoryRank(long userId, int gameId){
        LambdaQueryWrapper<HistoryRank> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HistoryRank::getUserId, userId)
                .eq(HistoryRank::getGameId, gameId);
        return this.getOne(lambdaQueryWrapper);
    }
}
