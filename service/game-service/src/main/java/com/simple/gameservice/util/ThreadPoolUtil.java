package com.simple.gameservice.util;

import com.simple.api.game.Room;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolUtil {

    private final static ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(100);

    private final static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,100,10, TimeUnit.SECONDS, queue,
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    public static void handlerTask(Room room){
        log.info("[{}]创建房间[{}]",room.getOwner().getUsername(),room.getRoomId());
        threadPoolExecutor.execute(room);
    }
}
