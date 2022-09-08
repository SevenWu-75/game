package com.simple.gameframe.util;

import com.simple.api.game.Player;
import com.simple.api.game.Room;

import java.security.Key;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class RoomPropertyManagerUtil {

    private final static Map<String, Condition> conditionMap = new ConcurrentHashMap<>();

    private final static Map<String, Lock> lockMap = new ConcurrentHashMap<>();

    private final static Map<String, CountDownLatch> countDownMap = new ConcurrentHashMap<>();

    private final static Map<String, Long> packageIdMap = new ConcurrentHashMap<>();

    private final static Map<String, Room<? extends Player>> roomMap = new ConcurrentHashMap<>();

    public static CountDownLatch getCountDownLatch(String roomId, int minPlayer) {
        if(countDownMap.containsKey(roomId))
            return countDownMap.get(roomId);
        CountDownLatch countDownLatch = new CountDownLatch(minPlayer);
        countDownMap.put(roomId, countDownLatch);
        return countDownLatch;
    }

    public static Lock getLock(String roomId){
        if(lockMap.containsKey(roomId))
            return lockMap.get(roomId);
        else {
            Lock lock = new ReentrantLock();
            lockMap.put(roomId,lock);
            return lock;
        }
    }

    public static Condition getCondition(String roomId, String logicId){
        if(conditionMap.containsKey(roomId + logicId))
            return conditionMap.get(roomId + logicId);
        else {
            Lock lock = getLock(roomId);
            Condition condition = lock.newCondition();
            conditionMap.put(roomId + logicId, condition);
            return condition;
        }
    }

    public static Long incrementAndGetPackageId(String roomId, String logicId){
        Set<String> keys = packageIdMap.keySet().stream().filter(key -> key.startsWith(roomId)).collect(Collectors.toSet());
        Optional<Long> max = packageIdMap.entrySet().stream().filter(set -> keys.contains(set.getKey()))
                .map(Map.Entry::getValue).max(Comparator.comparing(Long::valueOf));
        long id = max.orElse(0L) + 1;
        packageIdMap.put(roomId + logicId, id);
        return id;
    }

    public static Long getPackageIdMap(String roomId, String logicId) {
        if(packageIdMap.containsKey(roomId + logicId)){
            return packageIdMap.get(roomId + logicId);
        }
        return 0L;
    }

    public static void saveRoomImpl(Room<? extends Player> room){
        roomMap.put(room.getRoomId(), room);
    }

    public static Room<? extends Player> getRoomImpl(String roomId){
        return roomMap.get(roomId);
    }

    public static void clean(String roomId){
        List<String> collect = conditionMap.keySet().stream().filter(key -> key.startsWith(roomId)).collect(Collectors.toList());
        collect.forEach(conditionMap::remove);
        lockMap.remove(roomId);
        countDownMap.remove(roomId);
        roomMap.remove(roomId);
    }
}
