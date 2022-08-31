package com.simple.speedbootdice.vo;

import com.simple.api.game.entity.HistoryRank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class HistoryRankVo {

    private Long playCount = 0L;

    private Integer winCount = 0;

    private Long bestScore = 0L;

    private Date lastPlayTime;

    private Long winPercent;

    public Long getWinPercent(){
        return Math.round(winCount * 100.0 / playCount);
    }

    public HistoryRankVo(HistoryRank historyRank){
        this.playCount = historyRank.getPlayCount();
        this.winCount = historyRank.getWinCount();
        this.bestScore = historyRank.getBestScore();
        this.lastPlayTime = historyRank.getLastPlayTime();
    }
}
