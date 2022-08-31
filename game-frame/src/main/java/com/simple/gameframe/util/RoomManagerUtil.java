package com.simple.gameframe.util;

import com.simple.speedbootdice.pojo.Room;

import java.util.Map;

public class RoomManagerUtil {

    private static Map<String, Room> roomMap;

    public static Map<String, Room> getRoomMap(){
        if(roomMap == null){
            roomMap = (Map<String, Room>) ApplicationContextUtil.getBean("roomMap");
        }
        return roomMap;
    }

    public static Room getRoomByRoomId(String roomId){
        return getRoomMap().get(roomId);
    }

    public static void removeRoom(String roomId){
        getRoomMap().remove(roomId);
    }
}
