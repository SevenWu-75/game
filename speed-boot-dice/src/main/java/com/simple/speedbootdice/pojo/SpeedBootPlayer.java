package com.simple.speedbootdice.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.simple.api.game.Player;
import com.simple.api.game.UserVO;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.api.user.entity.User;
import com.simple.gameframe.core.ClassInject;
import com.simple.speedbootdice.common.ScoreEnum;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ClassInject("com.simple.api.game.Player")
@Data
@JsonIgnoreProperties(value = {"diceList","currentDices"})
public class SpeedBootPlayer implements Player {

    private int id;

    private UserVO user;

    private final transient List<Dice> diceList;

    private List<Integer> currentDices;

    private int status;

    private final int[] scores;

    private int[] currentScores;

    private int playTimes = 3;

    public SpeedBootPlayer(Integer id, UserVO user){
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
            throw new GameException(GameExceptionEnum.UPDATE_SCORE_FAILED);
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

    public void costPlayTimes(){
        this.playTimes--;
    }

    public void resetPlayTimes(){
        this.playTimes = 3;
    }

    public boolean enoughPlayTimes(){
        return this.playTimes != 0;
    }
}
