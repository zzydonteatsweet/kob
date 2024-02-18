package com.kob.backend.service.impl.user.bot;

import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {
    @Autowired
    BotMapper botMapper ;


    @Override
    public Map<String,String> add(Map<String,String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication() ;
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal() ;
        User user = loginUser.getUser() ;

        String name = data.get("name") ;
        String description = data.get("description") ;
        String content = data.get("content") ;

        Map<String, String> map = new HashMap<>() ;
        if(name == null || name.length() == 0) {
            map.put("error_message", "名字不能为空") ;
            return map ;
        }else if(description.length() > 300) {
            map.put("error_message", "描述长度过长") ;
            return map ;
        }else if(description == null || description.length() == 0) {
            map.put("error_message", "描述不能什么都没留下") ;
            return map ;
        }else if(content == null || content.length() == 0) {
            map.put("error_message", "代码不能为空") ;
            return map ;
        }else if(content.length() > 10000) {
            map.put("error_message", "代码过长") ;
            return map ;
        }
        Date now = new Date() ;
        Bot bot = new Bot(null, user.getId(), name, description,
                content, now, now) ;
//        System.out.println("Add User " + bot.getUser_id());
        botMapper.insert(bot) ;
        map.put("error_message", "success") ;
        return map ;

    }

}
