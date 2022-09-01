package com.simple.api.util;

import com.simple.api.user.entity.User;
import com.simple.api.game.Room;

public class ThreadLocalUtil {

    private static final ThreadLocal<User> USER_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<Room> ROOM_HOLDER = new ThreadLocal<>();

    public static void setUser(User user) {
        USER_HOLDER.set(user);
    }

    public static User getUser() {
        return USER_HOLDER.get();
    }

    public static void removeUser() {
        USER_HOLDER.remove();
    }

    public static void setRoom(Room room) {
        ROOM_HOLDER.set(room);
    }

    public static Room getRoom() {
        return ROOM_HOLDER.get();
    }

    public static void removeRoom() {
        ROOM_HOLDER.remove();
    }
}
