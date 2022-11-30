package com.simple.gameframe.core.listener;

import com.simple.api.game.Player;
import com.simple.api.game.service.HistoryRankService;
import com.simple.gameframe.common.GameCommand;
import com.simple.gameframe.core.event.GameResultEvent;
import com.simple.gameframe.util.MessagePublishUtil;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Value;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GameOverListener extends AbstractEventListener<GameResultEvent> {

    @DubboReference
    HistoryRankService historyRankService;

    @Value("${game-frame.game-id}")
    Integer gameId;

    @Override
    public boolean eventHandle(GameResultEvent event) {
        Player winner = null;
        List<? extends Player> playerList = event.getRoom().getPlayerList();
        Optional<? extends Player> winnerOptional = playerList.stream().max(Comparator.comparing(player -> player.getTotalScore()));
        if(winnerOptional.isPresent()){
            long count = playerList.stream().filter(
                            player -> player.getTotalScore() == winnerOptional.get().getTotalScore())
                    .count();
            if(count == 1){
                //非平局
                winner = winnerOptional.get();
            }
        }
        messageHandler.setCommand(GameCommand.GAME_RESULT);
        MessagePublishUtil.sendToRoomPublic(event.getRoom().getRoomId(), messageHandler.messageHandle(event.getRoom(), null, winner));

        //保存历史记录
        for (Player player : playerList) {
            historyRankService.saveHistory(player.getUser().getId(), gameId,
                    winner != null && winner.getUser().getId().equals(player.getUser().getId()),
                    player.getTotalScore());
        }
        return false;
    }
}
