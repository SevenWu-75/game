package com.simple.gameframe.core;

import lombok.Data;

import java.util.Random;

@Data
public class Dice {

    private int id;

    private Random random;

    private int currentNum;

    private int lockStatus;

    public Dice(int id){
        this.id = id;
        this.random = new Random();
        lockStatus = 1;
        currentNum = 6;
    }

    public int playDice(){
        currentNum = random.nextInt(6);
        return currentNum;
    }

    public void lockDice(){
        lockStatus = -1;
    }

    public void unlockDice(){
        lockStatus = 1;
    }
}
