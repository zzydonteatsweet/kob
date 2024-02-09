package com.kob.backend.consumer ;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JWTAuthenciation;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    private static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>() ;
    private static CopyOnWriteArraySet<User> matchPool  = new CopyOnWriteArraySet<>() ;
    private Session session = null ;
    private User user ;

    private static UserMapper userMapper ;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper ;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session = session ;
        System.out.println("WebSocket Connected");

        Integer userId = JWTAuthenciation.getUserId(token);
        System.out.println(userId);
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
            matchPool.remove(this.user) ;
        }
    }

    private void startMatching() {
        System.out.println("start-matching");
        matchPool.add(this.user) ;
        while(matchPool.size() > 1) {
            Iterator<User> it = matchPool.iterator();
            User a = it.next(), b = it.next() ;
            matchPool.remove(a); matchPool.remove(b) ;

            Game game = new Game(13, 14, 20) ;
            game.createMap();
            JSONObject respA = new JSONObject(), respB = new JSONObject() ;
            respA.put("event", "start-matching") ;
            respA.put("opponent_name", b.getUsername()) ;
            respA.put("opponent_photo", b.getPhoto()) ;
            respA.put("gamemap", game.getG()) ;
            users.get(a.getId()).sendMessage(respA.toJSONString());

            respB.put("event", "start-matching") ;
            respB.put("opponent_name", a.getUsername()) ;
            respB.put("opponent_photo", a.getPhoto()) ;
            respB.put("gamemap", game.getG()) ;
            System.out.println(respB.toJSONString());
            users.get(b.getId()).sendMessage(respB.toJSONString());
        }
    }

    private void stopMatching() {
        System.out.println("stop-matching");
        matchPool.remove(this.user) ;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("Received");
        JSONObject data = JSONObject.parseObject(message) ;
        String event = data.getString("event") ;
        if("start-matching".equals(event)) {
            startMatching();
        }else {
            stopMatching();
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
