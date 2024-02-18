package com.kob.botrunningsystem.service.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread{
    private Bot bot;
    private static RestTemplate restTemplate;
    private static final String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }
    public void startTimeout(long timeOut, Bot bot) {
        this.bot = bot;
        this.start();

        try {
            this.join(timeOut); //  最多等待timeout秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            this.interrupt();
        }
    }

    private String addUid(String code, String uid) {
        int k = code.indexOf(" implements com.kob.botrunningsystem.utils.BotInterface");
        return code.substring(0,k) + uid + code.substring(k);
    }

    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0,8);
        BotInterface botInterface = Reflect.compile(
            "com.kob.botrunningsystem.utils.Bot" + uid,
            addUid(bot.getBotCode(), uid)
        ).create().get();
        Integer direction = botInterface.nextMove(bot.getInput());
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("direction", direction.toString());
        data.add("user_id", bot.getUserId().toString());
        System.out.println("Bot running " + bot.getUserId() + " " + direction);
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
