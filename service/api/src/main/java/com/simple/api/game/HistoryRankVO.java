package com.simple.api.game;

import com.simple.api.game.entity.HistoryRank;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HistoryRankVO implements Serializable {

    private Long playCount = 0L;

    private Integer winCount = 0;

    private Long bestScore = 0L;

    private Date lastPlayTime;

    private int winPercent = 0;

    public HistoryRankVO(){

    }

    public HistoryRankVO(HistoryRank historyRank){
        this.setBestScore(historyRank.getBestScore());
        this.setPlayCount(historyRank.getPlayCount());
        this.setWinCount(historyRank.getWinCount());
        this.setLastPlayTime(historyRank.getLastPlayTime());
        this.winPercent = (int)Math.round(winCount * 100.0 / playCount);
    }
}
