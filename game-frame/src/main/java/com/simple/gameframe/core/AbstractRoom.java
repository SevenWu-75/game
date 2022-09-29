package com.simple.gameframe.core;

import com.simple.api.game.Player;
import com.simple.api.game.Room;
import com.simple.api.game.UserVO;
import com.simple.api.game.exception.GameException;
import com.simple.api.game.exception.GameExceptionEnum;
import com.simple.api.game.RoomStatusEnum;
import com.simple.gameframe.config.GameFrameProperty;
import com.simple.gameframe.util.PackageUtil;
import org.apache.dubbo.common.utils.ConcurrentHashSet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractRoom<T extends Player> implements Room<T> {

    private final GameFrameProperty gameFrameProperty;

    private final String roomId;

    private final UserVO owner;

    private final Set<UserVO> onlooker;

    private Date createTime;

    private List<T> playerList;

    protected int playAtLeastNum;

    protected int playCount;

    private int roomStatus;

    private Date startTime;

    private Date endTime;

    private int currentSeat;

    private long maxMessageId;

    private final static Random random = new Random();

    public AbstractRoom(UserVO user, GameFrameProperty gameFrameProperty){
        this.gameFrameProperty = gameFrameProperty;
        this.roomId = String.valueOf(generateRoomId());
        this.owner = user;
        this.onlooker = new ConcurrentHashSet<>();
        this.createTime = new Date();
        this.playerList = new LinkedList<>();
        this.roomStatus = RoomStatusEnum.created.ordinal();
    }

    public void join(UserVO user) {
        checkUser(user.getId());
        this.onlooker.add(user);
    }

    @Override
    public String getRoomId() {
        return this.roomId;
    }

    @Override
    public UserVO getOwner() {
        return this.owner;
    }

    @Override
    public int getRoomStatus() {
        return this.roomStatus;
    }

    @Override
    public Date getCreateTime() {
        return this.createTime;
    }

    @Override
    public Date getStartTime() {
        return this.startTime;
    }

    @Override
    public Date getEndTime() {
        return this.endTime;
    }

    @Override
    public List<T> getPlayerList() {
        return this.playerList;
    }

    @Override
    public Set<UserVO> getOnlooker() {
        return this.onlooker;
    }

    @Override
    public int getCurrentSeat() {
        return this.currentSeat;
    }

    @Override
    public void setCurrentSeat(int player) {
        this.currentSeat = player;
    }

    @Override
    public long getMaxMessageId(){
        return this.maxMessageId;
    }

    @Override
    public void setMaxMessageId(long messageId){
        if(this.maxMessageId < messageId)
            this.maxMessageId = messageId;
    }

    public void canStart(){
        this.roomStatus = RoomStatusEnum.canStart.ordinal();
    }

    public void start(){
        this.startTime = new Date();
        this.roomStatus = RoomStatusEnum.started.ordinal();
    }

    public void end(){
        this.endTime = new Date();
        this.roomStatus = RoomStatusEnum.over.ordinal();
    }

    public Player seatDown(UserVO user) {
        checkUser(user.getId());
        Class<?> playerImpl = PackageUtil.getPlayerImpl(gameFrameProperty.getScan());
        T player = null;
        try {
            //生成玩家实例
            Constructor<?> constructor = playerImpl.getConstructor(Integer.class, UserVO.class);
            player = (T)constructor.newInstance(generatePlayerId(),user);
            getPlayerList().add(player);
            getOnlooker().remove(user);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Player实现类未实现传入相应参数的构造函数！",e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Player实现类实例化失败！",e);
        }
        return player;
    }

    public void checkUser(Long userId) {
        Set<Long> userIdSet = getPlayerList().stream().map(p->p.getUser().getId()).collect(Collectors.toSet());
        if(userIdSet.contains(userId))
            throw new GameException(GameExceptionEnum.SEAT_REPEATED);
    }

    public int generatePlayerId(){
        return getPlayerList().stream().map(Player::getId).max(Integer::compare).map(integer -> integer + 1).orElse(0);
    }

    private int generateRoomId(){
        return random.nextInt(100000) + random.nextInt(9) * 100000 + 100000;
    }
}
