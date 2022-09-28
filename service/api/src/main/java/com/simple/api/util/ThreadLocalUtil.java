package com.simple.api.util;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.api.game.UserVO;

public class ThreadLocalUtil {

    private static final ThreadLocal<UserVO> USER_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<RoomVO<Player>> ROOM_VO_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<Room<Player>> ROOM_HOLDER = new ThreadLocal<>();

    public static void setUserVO(UserVO user) {
        USER_HOLDER.set(user);
    }

    public static UserVO getUserVO() {
        return USER_HOLDER.get();
    }

    public static void removeUser() {
        USER_HOLDER.remove();
    }

    public static void setRoomVO(RoomVO<Player> room) {
        ROOM_VO_HOLDER.set(room);
    }

    public static RoomVO<Player> getRoomVO() {
        return ROOM_VO_HOLDER.get();
    }

    public static void setRoom(Room<Player> room) {
        ROOM_HOLDER.set(room);
    }

    public static Room<Player> getRoom() {
        return ROOM_HOLDER.get();
    }

    public static void removeRoom() {
        ROOM_VO_HOLDER.remove();
    }
}
