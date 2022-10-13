package com.simple.api.game;

import com.simple.api.game.entity.HistoryRank;
import com.simple.api.user.entity.User;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private Long id;

    private String username;

    private String realname;

    private String avatar;

    private HistoryRankVO historyRankVO;

    public void setHistoryRankVO(HistoryRankVO historyRankVO){
        this.historyRankVO = historyRankVO;
    }

    public HistoryRankVO getHistoryRankVO(){
        return this.historyRankVO;
    }

    public UserVO(){

    }

    public UserVO(User user){
        this.setId(user.getId());
        this.setUsername(user.getUsername());
        this.setRealname(user.getRealname());
        this.setAvatar(user.getAvatar());
    }
}
