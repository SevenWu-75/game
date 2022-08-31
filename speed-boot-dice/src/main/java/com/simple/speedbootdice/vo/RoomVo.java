package com.simple.speedbootdice.vo;

import com.simple.speedbootdice.entity.User;
import com.simple.speedbootdice.pojo.SpeedBootPlayer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@Accessors(chain = true)
public class RoomVo {

    private String roomId;

    private int roomStatus;

    private Date createTime;

    private Date startTime;

    private UserVo createBy;

    private List<SpeedBootPlayer> players;

    private List<UserVo> onlooker;
}
