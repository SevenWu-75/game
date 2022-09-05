package com.simple.api.game.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HistoryRank implements Serializable {

    protected Long id;

    protected Integer gameId;

    protected Long userId;

    protected Long playCount;

    protected Integer winCount;

    protected Long bestScore;

    protected Date lastPlayTime;

}
