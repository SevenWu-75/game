package com.simple.gameframe.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AskAnswerLockConditionManager {

    private final static Map<String, Condition> conditionMap = new ConcurrentHashMap<>();

    private final static Map<String, Lock> lockMap = new ConcurrentHashMap<>();

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
}
