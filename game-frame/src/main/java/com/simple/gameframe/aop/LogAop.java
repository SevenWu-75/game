package com.simple.gameframe.aop;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.core.DefaultMessage;
import com.simple.gameframe.util.RoomPropertyManagerUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@Aspect
@Slf4j
public class LogAop {

    @Pointcut("execution(* com.simple.gameframe.core.ask.LogicHandler+.preHandle(..))")
    public void preHandle() {
    }

    @Pointcut("execution(* com.simple.gameframe.core.ask.LogicHandler+.messageHandle(..))")
    public void messageHandle() {
    }

    @Pointcut("execution(* com.simple.gameframe.core.ask.LogicHandler+.postHandle(..))")
    public void postHandle() {
    }

    @Pointcut("execution(* com.simple.gameframe.core.ask.LogicHandler+.ask(..))")
    public void ask() {
    }

    @Pointcut("execution(* com.simple.gameframe.core.ask.LogicHandler+.answer(..))")
    public void answer() {
    }

    @Around("preHandle() || messageHandle() || postHandle() || ask() || answer()")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        String[] split = pjp.getTarget().toString().split("\\.");
        String name = pjp.getSignature().getName();
        if("answer".equals(name)){
            Optional<Object> first = Arrays.stream(pjp.getArgs()).filter(arg -> arg instanceof DefaultMessage).findFirst();
            if(first.isPresent()){
                DefaultMessage<?> message = (DefaultMessage<?>) first.get();
                Room<Player> room = ThreadLocalUtil.getRoom();
                if (message.getId() != RoomPropertyManagerUtil.getPackageIdMap(room.getRoomId(), pjp.getThis().toString())) {
                    return null;
                }
                log.trace("接收到消息包id{},{}",message.getId(),message);
            }
        }
        log.trace("===执行{}的{}方法===",split[split.length-1], name);
        return pjp.proceed();
    }
}
