package com.simple.gameframe.core.listener;

import com.simple.gameframe.core.event.Event;

public interface EventListener<E extends Event> {

    void eventHandle(E event);
}
