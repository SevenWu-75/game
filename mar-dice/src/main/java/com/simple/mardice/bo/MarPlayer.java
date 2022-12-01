package com.simple.mardice.bo;

import com.simple.api.game.Player;
import com.simple.api.game.UserVO;
import com.simple.gameframe.core.ClassInject;
import com.simple.gameframe.core.Dice;
import com.simple.mardice.common.DiceNumEnum;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ClassInject("com.simple.api.game.Player")
public class MarPlayer implements Player, Serializable {

    private int id;

    private UserVO user;

    private int status;

    private List<Dice> diceList;

    private int totalScore;

    private List<Integer> scoreList;

    private int currentScore;

    private List<Dice> scoreDiceList;

    private List<Dice> autoTankDiceList;

    private List<Dice> spaceShipDiceList;

    public MarPlayer(Integer id, UserVO user){
        this.id = id;
        this.user = user;
        diceList = new LinkedList<Dice>(){{new Dice(0); new Dice(1); new Dice(2); new Dice(3); new Dice(4);
                new Dice(5); new Dice(6); new Dice(7); new Dice(8); new Dice(9);
                new Dice(10); new Dice(11); new Dice(12);}};
        scoreList = new LinkedList<>();
        scoreDiceList = new LinkedList<>();
        autoTankDiceList = new LinkedList<>();
        spaceShipDiceList = new LinkedList<>();
    }

    public void playDices(){
        diceList.forEach(Dice::playDice);
        //自动将投完骰子后出现的坦克塞入坦克骰子集合
        Iterator<Dice> iterator = diceList.iterator();
        while (iterator.hasNext()){
            Dice next = iterator.next();
            if(next.getCurrentNum() == DiceNumEnum.TANK.ordinal()){
                iterator.remove();
                autoTankDiceList.add(next);
            }
        }
    }

    public void selectDice(int diceNumEnum) {
        //将玩家选择的骰子塞入分数骰子集合
        boolean isExist = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == diceNumEnum);
        if(!isExist){
            Iterator<Dice> iterator = diceList.iterator();
            while (iterator.hasNext()){
                Dice next = iterator.next();
                if(next.getCurrentNum() == diceNumEnum){
                    iterator.remove();
                    scoreDiceList.add(next);
                }
            }
            calculateScore();
        }
    }

    public void resetDice(){
        diceList.forEach(Dice::unlockDice);
    }

    private void calculateScore(){
        long spaceShipCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_1.ordinal()
                || dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_2.ordinal()).count();
        long tankCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.TANK.ordinal()).count();
        if(spaceShipCount >= tankCount){
            long cowCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.COW.ordinal()).count();
            long chickenCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.CHICKEN.ordinal()).count();
            long manCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.MAN.ordinal()).count();
            int score = (int)cowCount + (int)chickenCount + (int)manCount;
            if(cowCount > 0 && chickenCount > 0 && manCount > 0){
                score += 3;
            }
            scoreList.add(score);
            currentScore = score;
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
        AtomicInteger sum = new AtomicInteger();
        scoreList.forEach(sum::addAndGet);
        return sum.get();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Dice> getDiceList() {
        return diceList;
    }

    public void setDiceList(List<Dice> diceList) {
        this.diceList = diceList;
    }

    public List<Integer> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<Integer> scoreList) {
        this.scoreList = scoreList;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public List<Dice> getScoreDiceList() {
        return scoreDiceList;
    }

    public void setScoreDiceList(List<Dice> scoreDiceList) {
        this.scoreDiceList = scoreDiceList;
    }

    public List<Dice> getAutoTankDiceList() {
        return autoTankDiceList;
    }

    public void setAutoTankDiceList(List<Dice> autoTankDiceList) {
        this.autoTankDiceList = autoTankDiceList;
    }

    public List<Dice> getSpaceShipDiceList() {
        return spaceShipDiceList;
    }

    public void setSpaceShipDiceList(List<Dice> spaceShipDiceList) {
        this.spaceShipDiceList = spaceShipDiceList;
    }
}
