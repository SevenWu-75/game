package com.simple.speedbootdice.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Random;

@Data
public class SpeedBootDice implements Serializable {

    private int id;

    private Random random;

    private int currentNum;

    private int lockStatus;

    public SpeedBootDice(int id){
        this.id = id;
        this.random = new Random();
        lockStatus = 1;
        currentNum = 6;
    }

    public int playDice(){
        currentNum = random.nextInt(6) + 1;
        return currentNum;
    }

    public void lockDice(){
        lockStatus = -1;
    }

    public void unlockDice(){
        lockStatus = 1;
    }
}
