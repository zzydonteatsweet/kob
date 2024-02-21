package com.kob.backend.controller.user.bot;

import com.kob.backend.pojo.Bot;
import com.kob.backend.service.user.bot.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {
    @Autowired
    QueryService queryService ;

    @GetMapping("/api/user/bot/getlist/")
    public List<Bot> queryBot() {
        return queryService.getList();
    }
}
