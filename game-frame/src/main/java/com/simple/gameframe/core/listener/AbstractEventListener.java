package com.simple.gameframe.core.listener;

import com.simple.gameframe.core.event.*;
import com.simple.gameframe.core.send.DefaultMessageHandler;

public abstract class AbstractEventListener<E extends Event> implements EventListener<E> {

    protected final DefaultMessageHandler messageHandler = new DefaultMessageHandler();
}
