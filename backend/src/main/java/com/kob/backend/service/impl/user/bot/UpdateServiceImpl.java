package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    BotMapper botMapper ;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication() ;
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal() ;

        Map<String,String> map = new HashMap<>() ;
        User user = loginUser.getUser() ;
        int bot_id = Integer.parseInt(data.get("bot_id")) ;
        Bot bot = botMapper.selectById(bot_id) ;

        String name = data.get("name") ;
        String description = data.get("description") ;
        String content = data.get("content") ;

        if(bot == null) {
            map.put("error_message", "机器不存在") ;
            return map ;
        }

        if(!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "你没有这个机器人的权限") ;
            return map ;
        }

        if(name == null || name.length() == 0) {
            map.put("error_message", "不能为空") ;
            return map ;
        }else if(description.length() > 300) {
            map.put("error_message", "描述长度过长") ;
            return map ;
        }else if(description == null || description.length() == 0) {
            map.put("error_message", "不能什么都没留下") ;
            return map ;
        }else if(content == null || content.length() == 0) {
            map.put("error_message", "代码不能为空") ;
            return map ;
        }else if(content.length() > 10000) {
            map.put("error_message", "代码过长") ;
            return map ;
        }

        Bot new_bot = new Bot(
                bot_id,
                user.getId(),
                name,
                description,
                content,
                bot.getRating(),
                bot.getCreateTime(),
                new Date()
        ) ;

        botMapper.updateById(new_bot) ;
        map.put("error_message", "success") ;
        return map ;
    }
}
