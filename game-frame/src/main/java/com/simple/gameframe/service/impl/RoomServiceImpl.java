package com.simple.gameframe.service.impl;

import com.simple.api.game.Room;
import com.simple.api.game.service.RoomService;
import com.simple.api.user.entity.User;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@DubboService
public class RoomServiceImpl implements RoomService {

    @Autowired
    Class<?> gameRoomClass;

    @Override
    public Room createRoom(User user) {
        Room room;
        try {
            Constructor<?> constructor = gameRoomClass.getConstructor(User.class);
            room = (Room)constructor.newInstance(user);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Room实现类未实现传入User类的构造函数！",e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Room实现类实例化失败！",e);
        }
        return room;
    }
}
