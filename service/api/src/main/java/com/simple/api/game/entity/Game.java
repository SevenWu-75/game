package com.simple.api.game.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Game implements Serializable {

    private Integer id;

    private String gameName;

    private String chineseName;

    private String version;
}
