package com.simple.speedbootdice.pojo;

import lombok.Data;

import java.util.Random;

@Data
public class Dice {

    private int id;

    private Random random;

    public Dice(int id){
        this.id = id;
        this.random = new Random();
    }

    public int playDice(){
        return random.nextInt(6) + 1;
    }
}
