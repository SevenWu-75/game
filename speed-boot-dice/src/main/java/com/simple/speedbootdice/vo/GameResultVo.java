package com.simple.speedbootdice.vo;

import com.simple.speedbootdice.bo.SpeedBootPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameResultVo {

    private SpeedBootPlayer winner;

    private List<SpeedBootPlayer> players;
}
