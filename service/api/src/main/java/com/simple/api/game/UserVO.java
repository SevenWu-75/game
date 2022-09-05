package com.simple.api.game;

import com.simple.api.game.entity.HistoryRank;
import com.simple.api.user.entity.User;
import lombok.Data;

@Data
public class UserVO extends User {

    private HistoryRankVO historyRankVO;

    public UserVO(User user, HistoryRank historyRank) {
        this(user);
        this.historyRankVO = new HistoryRankVO(historyRank);
    }

    public UserVO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.realname = user.getRealname();
    }
}
