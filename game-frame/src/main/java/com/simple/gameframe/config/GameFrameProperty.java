package com.simple.gameframe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(
        prefix = "game-frame"
)
public class GameFrameProperty {

    private String scan;

    public String getScan(){
        return this.scan;
    }

    public void setScan(String scan){
        this.scan = scan;
    }
}
