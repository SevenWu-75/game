package com.simple.api.user.entity;

import com.simple.api.game.entity.HistoryRank;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
public class User implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String realname;

    private String avatar;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
