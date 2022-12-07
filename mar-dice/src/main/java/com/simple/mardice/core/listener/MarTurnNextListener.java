package com.simple.mardice.core.listener;

import com.simple.gameframe.core.event.TurnNextEvent;
import com.simple.gameframe.core.listener.AbstractEventListener;
import com.simple.gameframe.util.ApplicationContextUtil;
import com.simple.mardice.common.MarCommand;
import com.simple.mardice.core.SelectOrEndLogicHandler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class MarTurnNextListener extends AbstractEventListener<TurnNextEvent> {

    @Override
    public boolean eventHandle(TurnNextEvent event) {
        SelectOrEndLogicHandler bean = ApplicationContextUtil.getBean(SelectOrEndLogicHandler.class);
        bean.setCommands(new ArrayList<MarCommand>()
        {{add(MarCommand.PLAY_DICE);add(MarCommand.SELECT_DICE);add(MarCommand.END_ROUND);}});
        return true;
    }
}
