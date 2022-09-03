package com.simple.speedbootdice.listener;

import com.simple.api.game.service.HistoryRankService;
import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.event.GameResultEvent;
import com.simple.gameframe.core.listener.AbstractEventListener;
import com.simple.speedbootdice.common.ScoreEnum;
import com.simple.speedbootdice.pojo.SpeedBootPlayer;
import com.simple.speedbootdice.pojo.SpeedBootRoom;
import com.simple.speedbootdice.vo.GameResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class SpeedBoatGameOverEventListener extends AbstractEventListener<GameResultEvent> {

    @DubboReference
    HistoryRankService historyRankService;

    @Override
    public void eventHandle(GameResultEvent event) {
        SpeedBootRoom room = (SpeedBootRoom)event.getRoom();
        List<SpeedBootPlayer> playerList = room.getPlayerList();
        SpeedBootPlayer winner = null;
        Optional<SpeedBootPlayer> winnerOptional = playerList.stream().max(Comparator.comparing(player -> player.getScores()[ScoreEnum.TOTAL_SUM.ordinal()]));
        if(winnerOptional.isPresent()){
            long count = playerList.stream().filter(
                            player -> player.getScores()[ScoreEnum.TOTAL_SUM.ordinal()] == winnerOptional.get().getScores()[ScoreEnum.TOTAL_SUM.ordinal()])
                    .count();
            if(count == 1){
                //非平局
                winner = winnerOptional.get();
            }
        }
        GameResultVo gameResultVo = new GameResultVo(winner, playerList);
        messageHandler.setCommand(GameCommand.GAME_RESULT);
        messageHandler.messageHandle(event.getRoom(), gameResultVo);

        //保存历史记录
        for (SpeedBootPlayer player : playerList) {
            historyRankService.saveHistory(player.getUser().getId(), 1,
                    winner != null && winner.getUser().getId().equals(player.getUser().getId()),
                    player.getScores()[ScoreEnum.TOTAL_SUM.ordinal()]);
        }
    }
}
