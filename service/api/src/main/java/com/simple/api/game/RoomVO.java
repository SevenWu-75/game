package com.simple.api.game;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
public class RoomVO<T extends Player> implements Serializable {

    private String roomId;

    private int roomStatus;

    private Date createTime;

    private Date startTime;

    private List<T> playerList;

    private Set<UserVO> onlooker;

    private UserVO owner;

    private Date endTime;

    private int playCount;

    private int playAtLeastNum;

    private int currentSeat;

    private long maxMessageId;

    public RoomVO(){

    }

    public RoomVO(Room<T> room){
        this.roomId = room.getRoomId();
        this.owner = room.getOwner();
        this.roomStatus = room.getRoomStatus();
        this.createTime = room.getCreateTime();
        this.endTime = room.getEndTime();
        this.startTime = room.getStartTime();
        this.playCount = room.getPlayCount();
        this.playerList = room.getPlayerList();
        this.playAtLeastNum = room.getPlayAtLeastNum();
        this.onlooker = room.getOnlooker();
        this.currentSeat = room.getCurrentSeat();
        this.maxMessageId = room.getMaxMessageId();
    }
}
