package com.simple.gameframe.core.listener;

import com.simple.api.game.Player;
import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.AbstractRoom;
import com.simple.gameframe.core.Message;
import com.simple.gameframe.core.event.*;
import com.simple.gameframe.util.MessagePublishUtil;
import com.simple.gameframe.util.RoomPropertyManagerUtil;

public class DefaultEventListener extends AbstractEventListener<Event> {

    @Override
    public void eventHandle(Event event) {
        AbstractRoom<? extends Player> abstractRoom = (AbstractRoom<? extends Player>) event.getRoom();
        GameCommand command = GameCommand.CREATE;
        if(event instanceof CreateEvent){
            RoomPropertyManagerUtil.saveRoomImpl(event.getRoom());
        }
        if(event instanceof CanStartEvent)
            command = GameCommand.CAN_START;
        if(event instanceof DisconnectEvent)
            command = GameCommand.DISCONNECT;
        if(event instanceof DismissEvent){
            command = GameCommand.DISMISS_ROOM;
            RoomPropertyManagerUtil.clean(event.getRoom().getRoomId());
            abstractRoom.end();
        }
        if(event instanceof GameOverEvent){
            command = GameCommand.GAME_OVER;
            RoomPropertyManagerUtil.clean(event.getRoom().getRoomId());
            abstractRoom.end();
        }
        if(event instanceof JoinEvent)
            command = GameCommand.JOIN;
        if(event instanceof ReconnectEvent)
            command = GameCommand.RECONNECT;
        if(event instanceof StartEvent){
            command = GameCommand.START_GAME;
            abstractRoom.start();
        }
        if(event instanceof TurnRoundEvent)
            command = GameCommand.TURN_ROUND;
        if(event instanceof TurnNextEvent)
            command = GameCommand.TURN_NEXT;
        if(event instanceof VoteDismissEvent)
            command = GameCommand.VOTE_DISMISS;
        if(event instanceof TimeOutEvent){
            command = GameCommand.TIMEOUT;
            RoomPropertyManagerUtil.clean(event.getRoom().getRoomId());
            abstractRoom.end();
        }
        if(event instanceof SeatDownEvent){
            command = GameCommand.SEAT_DOWN;
        }
        messageHandler.setCommand(command);
        Message<?> message = messageHandler.messageHandle(event.getRoom(), event.getO());
        MessagePublishUtil.sendToRoomPublic(event.getRoom().getRoomId(),message);
    }
}
