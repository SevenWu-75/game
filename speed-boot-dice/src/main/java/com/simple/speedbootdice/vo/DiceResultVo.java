package com.simple.speedbootdice.vo;

import lombok.Data;

import java.util.List;

@Data
public class DiceResultVo {

    List<Integer> numbers;

    int[] scores;

    int[] haveScores;

    int times;
}
