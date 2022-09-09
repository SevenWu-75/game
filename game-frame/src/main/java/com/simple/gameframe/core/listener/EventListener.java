package com.simple.gameframe.core.listener;

import com.simple.gameframe.core.event.Event;

public interface EventListener<E extends Event> {

    boolean eventHandle(E event);
}
