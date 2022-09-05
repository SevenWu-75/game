package com.simple.gameframe.util;

import com.alibaba.nacos.common.utils.ConcurrentHashSet;
import com.simple.api.game.Player;
import com.simple.api.user.entity.User;
import com.simple.api.util.ThreadLocalUtil;
import com.simple.gameframe.core.Message;
import com.simple.api.game.Room;
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
        if(message != null)
            message.setPrivateMessage(false);
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
        message.setPrivateMessage(false);
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
        if(message != null)
            message.setPrivateMessage(true);
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
        message.setPrivateMessage(true);
        setValidConfig(message);
        saveMessage(roomId, userId, message);
        //TODO: 保存当前房间所发送的最大messageId，保证玩家必须回复这个id才能进行流程
        //roomMap.get(roomId).getAskAnswerUtil().setMessageId(message.getId());
        getMessagingTemplate().convertAndSendToUser(userId, roomId, message);
        log.trace("发送id为{}的message包{}", message.getId(), message);
    }

    private static void setValidConfig(Message<?> message){
        Room<? extends Player> room = ThreadLocalUtil.getRoom();
        User user = ThreadLocalUtil.getUser();
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

    public static void sendMessageForReconnect(String roomId, String userId){
        Set<Message<?>> roomPublicMessages = reconnectMessageMap.get(roomId);
        Set<Message<?>> roomPrivateMessages = reconnectMessageMap.get(roomId + userId);
        List<Message<?>> allMessages = new ArrayList<>();
        if(!CollectionUtils.isEmpty(roomPublicMessages)){
            allMessages.addAll(roomPublicMessages);
        }
        if(!CollectionUtils.isEmpty(roomPrivateMessages)){
            allMessages.addAll(roomPrivateMessages);
        }
        List<Message<?>> collect = allMessages.stream().sorted(Comparator.comparing(Message::getId)).collect(Collectors.toList());
        collect.forEach(c->{
            if (c.getPrivateMessage()) {
                c.setReconnectUserId(Long.parseLong(userId));
                sendToRoomUser(userId, roomId, c);
            } else {
                c.setReconnectUserId(Long.parseLong(userId));
                sendToRoomPublic(roomId, c);
            }
        });
    }
}
