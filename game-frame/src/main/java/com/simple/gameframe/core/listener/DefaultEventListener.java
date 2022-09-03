package com.simple.gameframe.core.listener;

import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.event.*;

public class DefaultEventListener extends AbstractEventListener<Event> {

    @Override
    public void eventHandle(Event event) {
        GameCommand command = GameCommand.CREATE;
        if(event instanceof CreateEvent)
            command = GameCommand.CREATE;
        if(event instanceof CanStartEvent)
            command = GameCommand.CAN_START;
        if(event instanceof DisconnectEvent)
            command = GameCommand.DISCONNECT;
        if(event instanceof DismissEvent)
            command = GameCommand.DISMISS_ROOM;
        if(event instanceof GameOverEvent)
            command = GameCommand.GAME_OVER;
        if(event instanceof JoinEvent)
            command = GameCommand.JOIN;
        if(event instanceof ReconnectEvent)
            command = GameCommand.RECONNECT;
        if(event instanceof StartEvent)
            command = GameCommand.START_GAME;
        if(event instanceof TurnRoundEvent)
            command = GameCommand.TURN_ROUND;
        if(event instanceof TurnNextEvent)
            command = GameCommand.TURN_NEXT;
        if(event instanceof VoteDismissEvent)
            command = GameCommand.VOTE_DISMISS;
        if(event instanceof TimeOutEvent){
            command = GameCommand.TIMEOUT;
        }
        messageHandler.setCommand(command);
        messageHandler.messageHandle(event.getRoom(),event.getO());
    }
}
