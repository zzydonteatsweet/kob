package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if(game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    if (!game.getPlayerA().getBotId().equals(-1)) {
                        game.setNextStepA(direction);
                        System.out.println("A got it");
                    }
                } else if (game.getPlayerB().getId().equals(userId)) {
                    if (!game.getPlayerA().getBotId().equals(-1)) {
                        game.setNextStepB(direction);
                        System.out.println("B got it");
                    }
                }
            }
        }
        return "receive bot move success";
    }
}