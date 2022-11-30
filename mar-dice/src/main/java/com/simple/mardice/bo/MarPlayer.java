package com.simple.mardice.bo;

import com.simple.api.game.Player;
import com.simple.api.game.UserVO;
import com.simple.gameframe.core.ClassInject;
import com.simple.gameframe.core.Dice;
import com.simple.mardice.common.DiceNumEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ClassInject("com.simple.api.game.Player")
@Data
public class MarPlayer implements Player, Serializable {

    private int id;

    private UserVO user;

    private int status;

    private List<Dice> diceList;

    private int totalScore;

    private List<Integer> scoreList;

    public MarPlayer(Integer id, UserVO user){
        this.id = id;
        this.user = user;
        diceList = Arrays.asList(new Dice(0), new Dice(1), new Dice(2), new Dice(3), new Dice(4),
                new Dice(5), new Dice(6), new Dice(7), new Dice(8), new Dice(9),
                new Dice(10), new Dice(11), new Dice(12));
    }

    public void playDices(){
        diceList.stream().filter(dice -> dice.getLockStatus() != -1).forEach(Dice::playDice);
    }

    public void resetDice(){
        diceList.forEach(Dice::unlockDice);
    }

    public void calculateScore(){
        List<Dice> diceList = getDiceList();
        long spaceShipCount = diceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_1.ordinal()
                || dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_2.ordinal()).count();
        long tankCount = diceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.TANK.ordinal()).count();
        if(spaceShipCount >= tankCount){
            long cowCount = diceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.COW.ordinal()).count();
            long chickenCount = diceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.CHICKEN.ordinal()).count();
            long manCount = diceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.MAN.ordinal()).count();
            int score = (int)cowCount + (int)chickenCount + (int)manCount;
            if(cowCount > 0 && chickenCount > 0 && manCount > 0){
                score += 3;
            }
            scoreList.add(score);
            score += score;
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public UserVO getUser() {
        return user;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public int getTotalScore() {
        return totalScore;
    }
}
