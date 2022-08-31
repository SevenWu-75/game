package com.simple.speedbootdice.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.simple.api.common.SpeedBootException;
import com.simple.api.common.SpeedBootExceptionEnum;
import com.simple.api.game.Player;
import com.simple.api.user.entity.User;
import com.simple.speedbootdice.common.ScoreEnum;
import com.simple.speedbootdice.vo.UserVo;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SpeedBootPlayer implements Player {

    private int id;

    private User user;

    @JsonIgnore
    private final List<Dice> diceList;

    @JsonIgnore
    private List<Integer> currentDices;

    private int status;

    private final int[] scores;

    public SpeedBootPlayer(int id, User user){
        this.id = id;
        this.user = user;
        diceList = Arrays.asList(new Dice(0), new Dice(1), new Dice(2), new Dice(3), new Dice(4));
        scores = new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,0,0,0};
    }

    public List<Integer> playDices(int[] lockDice){
        if(currentDices == null){
            currentDices = diceList.stream().map(Dice::playDice).collect(Collectors.toList());
        } else {
            for (int i = 0; i < lockDice.length; i++) {
                if (lockDice[i] == -1) {
                    currentDices.set(i, diceList.get(i).playDice());
                }
            }
        }
        return currentDices;
    }

    public void updateScores(int index, int score){
        if(index > ScoreEnum.TOTAL_SUM.ordinal()){
            throw new SpeedBootException(SpeedBootExceptionEnum.UPDATE_SCORE_FAILED);
        }
        this.scores[index] = score;
        this.scores[ScoreEnum.TOTAL_SUM.ordinal()] += score;
        if(index <= ScoreEnum.SIX.ordinal())
            this.scores[ScoreEnum.SMALL_SUM.ordinal()] += score;
        if(this.scores[ScoreEnum.BONUS.ordinal()] != 35 && this.scores[ScoreEnum.SMALL_SUM.ordinal()] >= 63){
            this.scores[ScoreEnum.BONUS.ordinal()] = 35;
            this.scores[ScoreEnum.TOTAL_SUM.ordinal()] += 35;
        }
    }
}
