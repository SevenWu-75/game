package com.simple.api.game.entity;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryRank {

    private Long id;

    private Integer gameId;

    private Long userId;

    private Long playCount;

    private Integer winCount;

    private Long bestScore;

    private Date lastPlayTime;

}
