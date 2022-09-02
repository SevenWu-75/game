package com.simple.gameframe.service.impl;

import com.simple.api.game.Room;
import com.simple.api.game.service.RoomService;
import com.simple.api.user.entity.User;
import com.simple.gameframe.core.RoundHandler;
import com.simple.gameframe.util.ThreadPoolUtil;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@DubboService
@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    Class<?> gameRoomClass;

    @Autowired
    RoundHandler roundHandler;

    @Override
    public Room createRoom(User user) {
        Room room;
        try {
            //生成房间实例
            Constructor<?> constructor = gameRoomClass.getConstructor(User.class);
            room = (Room)constructor.newInstance(user);
            //执行房间运行逻辑
            roundHandler.getLock().lock();
            roundHandler.setRoom(room);
            ThreadPoolUtil.handlerTask(roundHandler);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Room实现类未实现传入User类的构造函数！",e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Room实现类实例化失败！",e);
        }
        return room;
    }
}
