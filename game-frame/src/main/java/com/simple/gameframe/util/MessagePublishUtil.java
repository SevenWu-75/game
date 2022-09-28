package com.simple.gameframe.util;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.RoomVO;
import com.simple.api.game.UserVO;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.core.Message;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class MessagePublishUtil {

    private static final String PREFIX_PUBLIC = "/topic";

    private static final String PREFIX_SYSTEM = "/system";

    private static SimpMessagingTemplate messagingTemplate;

    private static final Map<String, Set<Message<?>>> reconnectMessageMap = new HashMap<>();

    private static SimpMessagingTemplate getMessagingTemplate(){
        if(messagingTemplate == null){
            messagingTemplate = ApplicationContextUtil.getSimpMessageTemplate();
        }
        return messagingTemplate;
    }

    /**
     * 全局系统消息广播
     *
     * @param message 消息体
     */
    public static void sendToSystemPublic(Message<?> message){
        setValidConfig(message);
        getMessagingTemplate().convertAndSend(PREFIX_PUBLIC + PREFIX_SYSTEM, message == null ? "" : message);
    }

    /**
     * 房间内的消息广播
     *
     * @param roomId 房间id
     * @param message 消息体
     */
    public static void sendToRoomPublic(String roomId, @NotNull Message<?> message){
        setValidConfig(message);
        saveMessage(roomId, "", message);
        getMessagingTemplate().convertAndSend(PREFIX_PUBLIC + "/" + roomId, message);
        log.trace("发送id为{}的message包{}", message.getId(), message);
    }

    /**
     * 全局系统消息私发
     *
     * @param userId 用户id
     * @param message 消息体
     */
    public static void sendToSystemUser(String userId, Message<?> message){
        setValidConfig(message);
        getMessagingTemplate().convertAndSendToUser(userId, PREFIX_SYSTEM, message == null ? "" : message);
    }

    /**
     * 房间内消息私发
     *
     * @param userId 用户id
     * @param roomId 房间id
     * @param message 消息体
     */
    public static void sendToRoomUser(String userId, String roomId, @NotNull Message<?> message){
        setValidConfig(message);
        saveMessage(roomId, userId, message);
        //TODO: 保存当前房间所发送的最大messageId，保证玩家必须回复这个id才能进行流程
        //roomMap.get(roomId).getAskAnswerUtil().setMessageId(message.getId());
        getMessagingTemplate().convertAndSendToUser(userId, roomId, message);
        log.trace("发送id为{}的message包{}", message.getId(), message);
    }

    private static void setValidConfig(Message<?> message){
        Room<Player> room = ThreadLocalUtil.getRoom();
        UserVO user = ThreadLocalUtil.getUserVO();
        if(room != null && message != null){
            message.setRoomId(room.getRoomId());
        }
        if(user != null && message != null){
            message.setFromId(user.getId());
        }
    }

    private static void saveMessage(String roomId, String userId, Message<?> message){
        String key = roomId + userId;
        if(reconnectMessageMap.containsKey(key)){
            Set<Message<?>> messages = reconnectMessageMap.get(key);
            messages.add(message);
        } else {
            Set<Message<?>> messageList = new ConcurrentHashSet<>();
            messageList.add(message);
            reconnectMessageMap.put(key, messageList);
        }
    }
}
