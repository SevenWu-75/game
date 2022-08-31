package com.simple.speedbootdice.vo;

import com.simple.api.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UserVo {

    private Long id;

    private String username;

    private String realname;

    private HistoryRankVo historyRankVo;

    public UserVo(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.realname = user.getRealname();
    }
}
