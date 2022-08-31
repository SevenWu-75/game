package com.simple.gameframe.aop;

import com.simple.api.game.Message;
import com.simple.api.game.Room;
import com.simple.api.util.ThreadLocalUtil;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MessageHandlerAop {

    @SneakyThrows
    @Around("execution(* com.simple.gameframe.controller.CommandController.*(com.simple.api.game.Message))")
    public Object handleMessage(ProceedingJoinPoint pjp){
        Object[] args = pjp.getArgs();
        if(args.length == 1){
            Object arg = args[0];
            if(arg instanceof Message){
                Message<?> message = (Message<?>) arg;
                Room room = ThreadLocalUtil.getRoom();
                boolean isContinue = room.getAskAnswerUtil().confirmId(message.getId());
                if(isContinue){
                    return pjp.proceed();
                }
            }
        }
        return null;
    }
}
