package com.simple.gameframe.vo;

import com.simple.api.game.entity.HistoryRank;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class HistoryRankVO extends HistoryRank {

    private int winPercent;

    public HistoryRankVO(@NotNull HistoryRank historyRank){
        this.setUserId(historyRank.getUserId());
        this.setId(historyRank.getId());
        this.setBestScore(historyRank.getBestScore());
        this.setGameId(historyRank.getGameId());
        this.setPlayCount(historyRank.getPlayCount());
        this.setWinCount(historyRank.getWinCount());
        this.setLastPlayTime(historyRank.getLastPlayTime());
    }
}
