package com.simple.api.game.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.simple.api.game.entity.HistoryRank;

public interface HistoryRankService extends IService<HistoryRank> {

    void saveHistory(long userId, int gameId, boolean isWinner, long score);

    HistoryRank getHistoryRank(long userId, int gameId);
}
