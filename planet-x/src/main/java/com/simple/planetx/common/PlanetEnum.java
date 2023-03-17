package com.simple.planetx.common;

public enum PlanetEnum {

    EMPTY(0, "empty", "空域"),
    ASTEROID(1, "asteroid", "小行星"),
    DWARF_PLANET(2, "dwarf planet", "矮行星"),
    COMET(3, "comet", "彗星"),
    GASEOUS_CLOUD(4, "Gaseous cloud", "气体云"),
    PLANET_X(9, "planet x", "行星X"),
    ;

    private int code;

    private String name;

    private String chineseName;

    PlanetEnum(int code, String name, String chineseName){
        this.code = code;
        this.name = name;
        this.chineseName = chineseName;
    }

    public static PlanetEnum getByCode(int code) {
        if(EMPTY.getCode() == code) {
            return EMPTY;
        }
        if(ASTEROID.getCode() == code) {
            return ASTEROID;
        }
        if(DWARF_PLANET.getCode() == code) {
            return DWARF_PLANET;
        }
        if(COMET.getCode() == code) {
            return COMET;
        }
        if(GASEOUS_CLOUD.getCode() == code) {
            return GASEOUS_CLOUD;
        }
        if(PLANET_X.getCode() == code) {
            return PLANET_X;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getChineseName() {
        return chineseName;
    }
}
