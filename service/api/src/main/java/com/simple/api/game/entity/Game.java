package com.simple.api.game.entity;

import lombok.Data;

@Data
public class Game {

    private Integer id;

    private String gameName;

    private String chineseName;

    private String version;
}
