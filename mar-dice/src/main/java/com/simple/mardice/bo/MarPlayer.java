package com.simple.mardice.bo;

import com.simple.api.game.Player;
import com.simple.api.game.UserVO;
import com.simple.gameframe.core.ClassInject;
import com.simple.mardice.common.DiceNumEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@ClassInject("com.simple.api.game.Player")
@Data
public class MarPlayer implements Player, Serializable {

    private int id;

    private UserVO user;

    private int status;

    private List<MarDice> diceList;

    private int totalScore;

    private List<Integer> scoreList;

    private int currentScore;

    private List<MarDice> scoreDiceList;

    private List<MarDice> autoTankDiceList;

    private List<MarDice> spaceShipDiceList;

    private Boolean canDice;

    public MarPlayer(Integer id, UserVO user){
        this.id = id;
        this.user = user;
        diceList = new LinkedList<MarDice>(){{ add(new MarDice(0)); add(new MarDice(1)); add(new MarDice(2));
            add(new MarDice(3)); add(new MarDice(4)); add(new MarDice(5)); add(new MarDice(6)); add(new MarDice(7));
            add(new MarDice(8)); add(new MarDice(9)); add(new MarDice(10)); add(new MarDice(11)); add(new MarDice(12));}};
        scoreList = new LinkedList<>();
        scoreDiceList = new LinkedList<>();
        autoTankDiceList = new LinkedList<>();
        spaceShipDiceList = new LinkedList<>();
        canDice = false;
    }

    public void playDices(){
        diceList.forEach(MarDice::playDice);
        //自动将投完骰子后出现的坦克塞入坦克骰子集合
        Iterator<MarDice> iterator = diceList.iterator();
        while (iterator.hasNext()){
            MarDice next = iterator.next();
            if(next.getCurrentNum() == DiceNumEnum.TANK.ordinal()){
                iterator.remove();
                autoTankDiceList.add(next);
            }
        }
        diceList.sort(Comparator.comparing(MarDice::getCurrentNum));
        boolean cow = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == DiceNumEnum.COW.ordinal());
        boolean chicken = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == DiceNumEnum.CHICKEN.ordinal());
        boolean man = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == DiceNumEnum.MAN.ordinal());
        diceList.forEach(dice -> {
            if(dice.getCurrentNum() == DiceNumEnum.COW.ordinal()){
                if(cow)
                    dice.lockDice();
                else
                    dice.unlockDice();
            }
            if(dice.getCurrentNum() == DiceNumEnum.CHICKEN.ordinal()){
                if(chicken)
                    dice.lockDice();
                else
                    dice.unlockDice();
            }
            if(dice.getCurrentNum() == DiceNumEnum.MAN.ordinal()){
                if(man)
                    dice.lockDice();
                else
                    dice.unlockDice();
            }
            if(dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_2.ordinal() || dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_1.ordinal()){
                dice.unlockDice();
            }

        });
        canDice = false;
    }

    public void selectDice(int diceNumEnum) {
        //将玩家选择的骰子塞入分数骰子集合
        boolean isExist = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == diceNumEnum);
        if(!isExist){
            Iterator<MarDice> iterator = diceList.iterator();
            while (iterator.hasNext()){
                MarDice next = iterator.next();
                if(diceNumEnum == DiceNumEnum.SPACE_SHIP_1.ordinal() || diceNumEnum == DiceNumEnum.SPACE_SHIP_2.ordinal()){
                    if(next.getCurrentNum() == DiceNumEnum.SPACE_SHIP_1.ordinal() || next.getCurrentNum() == DiceNumEnum.SPACE_SHIP_2.ordinal()){
                        iterator.remove();
                        spaceShipDiceList.add(next);
                    }
                } else {
                    if(next.getCurrentNum() == diceNumEnum){
                        iterator.remove();
                        scoreDiceList.add(next);
                    }
                }
            }
            calculateScore();
            scoreDiceList.sort(Comparator.comparing(MarDice::getCurrentNum));
        }
        canDice = true;
    }

    public void resetDice(){
        Iterator<MarDice> iterator = scoreDiceList.iterator();
        while (iterator.hasNext()) {
            MarDice next = iterator.next();
            iterator.remove();
            diceList.add(next);
        }
        iterator = autoTankDiceList.iterator();
        while (iterator.hasNext()) {
            MarDice next = iterator.next();
            iterator.remove();
            diceList.add(next);
        }
        iterator = spaceShipDiceList.iterator();
        while (iterator.hasNext()) {
            MarDice next = iterator.next();
            iterator.remove();
            diceList.add(next);
        }
        diceList.forEach(MarDice::unlockDice);
    }

    public void calculateScore(){
        long spaceShipCount = spaceShipDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_1.ordinal()
                || dice.getCurrentNum() == DiceNumEnum.SPACE_SHIP_2.ordinal()).count();
        long tankCount = autoTankDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.TANK.ordinal()).count();
        if(spaceShipCount >= tankCount){
            long cowCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.COW.ordinal()).count();
            long chickenCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.CHICKEN.ordinal()).count();
            long manCount = scoreDiceList.stream().filter(dice -> dice.getCurrentNum() == DiceNumEnum.MAN.ordinal()).count();
            int score = (int)cowCount + (int)chickenCount + (int)manCount;
            if(cowCount > 0 && chickenCount > 0 && manCount > 0){
                score += 3;
            }
            currentScore = score;
        } else {
            currentScore = 0;
        }
    }

    public void enableDice(){
        //如果分数都拿够了，并且确保飞碟数大于坦克数则无需继续投掷骰子
        boolean cow = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == DiceNumEnum.COW.ordinal());
        boolean chicken = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == DiceNumEnum.CHICKEN.ordinal());
        boolean man = scoreDiceList.stream().anyMatch(dice -> dice.getCurrentNum() == DiceNumEnum.MAN.ordinal());
        canDice = (!cow || !chicken || !man);
        if(!canDice && spaceShipDiceList.size() < autoTankDiceList.size()) {
            canDice = true;
        }
        //如果骰子没有了或者剩下骰子全是飞碟也达不到坦克数量则无需继续投掷骰子
        if(diceList.size() == 0 || diceList.size() + spaceShipDiceList.size() < autoTankDiceList.size()) {
            canDice = false;
        }
    }

    @Override
    public int getTotalScore() {
        AtomicInteger sum = new AtomicInteger();
        scoreList.forEach(sum::addAndGet);
        return sum.get();
    }
}
