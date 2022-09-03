package com.simple.gameframe.core.listener;

import com.simple.gameframe.common.Command;
import com.simple.gameframe.core.event.*;
import com.simple.gameframe.core.send.DefaultMessageHandler;
import com.simple.gameframe.core.send.MessageHandler;

public class DefaultEventListener extends AbstractEventListener<Event> {

    @Override
    public void eventHandle(Event event) {
        Command command = Command.CREATE;
        if(event instanceof CreateEvent)
            command = Command.CREATE;
        if(event instanceof CanStartEvent)
            command = Command.CAN_START;
        if(event instanceof DisconnectEvent)
            command = Command.DISCONNECT;
        if(event instanceof DismissEvent)
            command = Command.DISMISS_ROOM;
        if(event instanceof GameOverEvent)
            command = Command.GAME_OVER;
        if(event instanceof JoinEvent)
            command = Command.JOIN;
        if(event instanceof ReconnectEvent)
            command = Command.RECONNECT;
        if(event instanceof StartEvent)
            command = Command.START_GAME;
        if(event instanceof TurnRoundEvent)
            command = Command.TURN_ROUND;
        if(event instanceof TurnNextEvent)
            command = Command.TURN_NEXT;
        if(event instanceof VoteDismissEvent)
            command = Command.VOTE_DISMISS;
        if(event instanceof TimeOutEvent){
            command = Command.TIMEOUT;
        }
        messageHandler.setCommand(command);
        messageHandler.messageHandle(event.getRoom(),event.getO());
    }
}
