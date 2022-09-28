package com.simple.gameframe.service.impl;

import com.simple.api.game.*;
import com.simple.api.game.entity.HistoryRank;
import com.simple.api.game.service.HistoryRankService;
import com.simple.api.game.service.RoomService;
import com.simple.api.user.entity.User;
import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.core.RoomHandler;
import com.simple.gameframe.util.PackageUtil;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import com.simple.gameframe.util.ThreadPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@DubboService
@Service
@Slf4j
public class RoomServiceImpl implements RoomService {

    @Autowired
    GameFrameProperty gameFrameProperty;

    @Autowired
    RoomHandler roomHandler;

    @Override
    public RoomVO<? extends Player> createRoom(UserVO user) {
        Room<Player> room;
        try {
            Class<?> roomImpl = PackageUtil.getRoomImpl(gameFrameProperty.getScan());
            //生成房间实例
            Constructor<?> constructor = roomImpl.getConstructor(UserVO.class, GameFrameProperty.class);
            room = (Room<Player>)constructor.newInstance(user, gameFrameProperty);
            log.trace("创建房间成功");
            //执行房间运行逻辑
            roomHandler.setRoom(room);
            ThreadPoolUtil.handlerTask(roomHandler);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Room实现类未实现传入User类的构造函数！",e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Room实现类实例化失败！",e);
        }
        return new RoomVO<>(room);
    }

    @Override
    public RoomVO<? extends Player> getRoom(String roomId) {
        Room<Player> roomImpl = RoomPropertyManagerUtil.getRoomImpl(roomId);
        if(roomImpl == null)
            return null;
        return new RoomVO<>(roomImpl);
    }
}
