package com.simple.gameservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simple.api.game.entity.Game;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GameMapper extends BaseMapper<Game> {
}
