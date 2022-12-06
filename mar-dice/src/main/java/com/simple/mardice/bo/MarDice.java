package com.simple.mardice.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Random;

@Data
public class MarDice implements Serializable {

    private int id;

    private transient Random random;

    private int currentNum;

    private int lockStatus;

    public MarDice(int id){
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
