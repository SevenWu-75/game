package com.simple.gameframe.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@ConfigurationProperties(
        prefix = "game-frame"
)
public class GameFrameProperty implements Serializable {

    private int gameId;

    public int getGameId() {
        return this.gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    private String scan;

    public String getScan(){
        return this.scan;
    }

    public void setScan(String scan){
        this.scan = scan;
    }

    private boolean enableLinkLogic;

    public boolean getEnableLinkLogic(){
        return this.enableLinkLogic;
    }

    public void setEnableLinkLogic(boolean enableLinkLogic){
        this.enableLinkLogic = enableLinkLogic;
    }
}
