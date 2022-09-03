package com.simple.api.game.service;

import com.simple.api.game.entity.HistoryRank;

public interface HistoryRankService {

    void saveHistory(long userId, int gameId, boolean isWinner, long score);

    HistoryRank getHistoryRank(long userId, int gameId);
}
