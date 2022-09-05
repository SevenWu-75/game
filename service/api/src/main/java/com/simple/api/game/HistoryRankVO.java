package com.simple.api.game;

import com.simple.api.game.entity.HistoryRank;
import lombok.Data;

@Data
public class HistoryRankVO extends HistoryRank {

    private int winPercent;

    public HistoryRankVO(HistoryRank historyRank){
        this.setUserId(historyRank.getUserId());
        this.setId(historyRank.getId());
        this.setBestScore(historyRank.getBestScore());
        this.setGameId(historyRank.getGameId());
        this.setPlayCount(historyRank.getPlayCount());
        this.setWinCount(historyRank.getWinCount());
        this.setLastPlayTime(historyRank.getLastPlayTime());
    }
}
