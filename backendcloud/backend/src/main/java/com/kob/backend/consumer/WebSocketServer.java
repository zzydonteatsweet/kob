package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JWTAuthenciation;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
 import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>() ;
    private Session session = null ;
    private User user ;

    public static UserMapper userMapper ;
    public static RecordMapper recordMapper ;
    public static RestTemplate restTemplate;
    private static BotMapper botMapper;
    public Game game = null ;
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:3001/player/remove/";
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper ;
    }
    @Autowired
    public void setRecordMappeer(RecordMapper recordMappeer) {
        WebSocketServer.recordMapper = recordMappeer ;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }
    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session ;
        System.out.println("WebSocket Connected");

        Integer userId = JWTAuthenciation.getUserId(token);
        this.user = userMapper.selectById(userId) ;
        if(this.user == null) {
            try {
                this.session.close();
            } catch (Exception e) {
                throw new RuntimeException(e) ;
            }
        }else {
           users.put(userId, this) ;
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("Closed");
        if(this.user != null) {
            users.remove(this.user.getId()) ;
        }
    }

    public static void startGame(Integer aId, Integer aBotId ,Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);
        Game game = new Game(
                13,
                14,
                20,
                a.getId(),
                b.getId(),
                botA,
                botB) ;
        game.createMap();
        game.start();
        System.out.println("userId " + a.getId() + " " + b.getId());
        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = game ;
        if(users.get(b.getId()) != null)
            users.get(b.getId()).game = game ;
        JSONObject respGame = new JSONObject() ;
        respGame.put("a_id", game.getPlayerA().getId()) ;
        respGame.put("a_sx", game.getPlayerA().getSx()) ;
        respGame.put("a_sy", game.getPlayerA().getSy()) ;

        respGame.put("b_id", game.getPlayerB().getId()) ;
        respGame.put("b_sx", game.getPlayerB().getSx()) ;
        respGame.put("b_sy", game.getPlayerB().getSy()) ;

        respGame.put("map", game.getG()) ;
        JSONObject respA = new JSONObject(), respB = new JSONObject() ;
        respA.put("event", "start-matching") ;
        respA.put("opponent_name", b.getUsername()) ;
        respA.put("opponent_photo", b.getPhoto()) ;
        respA.put("game", respGame) ;

        respB.put("event", "start-matching") ;
        respB.put("opponent_name", a.getUsername()) ;
        respB.put("opponent_photo", a.getPhoto()) ;
        respB.put("game", respGame) ;
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }
    private void startMatching(Integer bot_id) {
        System.out.println("start-matching");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        data.add("bot_id", bot_id.toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    private void stopMatching() {
        System.out.println("stop-matching");
        MultiValueMap<String ,String > data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString()) ;
        restTemplate.postForObject(removePlayerUrl, data, String.class);
    }

    private void move(int direction) {
        if(game.getPlayerA().getId().equals(user.getId())) {
            if(game.getPlayerA().getBotId().equals(-1))
                game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())) {
            if(game.getPlayerB().getBotId().equals(-1)) {
                game.setNextStepB(direction);
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        JSONObject data = JSONObject.parseObject(message) ;
        String event = data.getString("event") ;
        System.out.println("Received " + event);
        if("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)) {
            move(data.getInteger("direction")) ;
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
