package com.simple.gameframe.vo;

import com.simple.api.game.entity.HistoryRank;
import com.simple.api.user.entity.User;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class UserVO extends User {

    private HistoryRankVO historyRankVO;

    public UserVO(@NotNull User user, HistoryRank historyRank) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.realname = user.getRealname();
        this.historyRankVO = new HistoryRankVO(historyRank);
    }
}
