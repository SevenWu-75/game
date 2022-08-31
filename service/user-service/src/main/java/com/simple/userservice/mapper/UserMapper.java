package com.simple.userservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.simple.api.user.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
