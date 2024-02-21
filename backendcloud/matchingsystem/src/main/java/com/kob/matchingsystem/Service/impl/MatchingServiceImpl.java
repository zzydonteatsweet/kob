package com.kob.matchingsystem.Service.impl;

import com.kob.matchingsystem.Service.MatchingService;
import com.kob.matchingsystem.Service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool matchingPool = new MatchingPool();
    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        matchingPool.addPlayer(userId,rating, botId);
        return "add Player Success";
    }

    @Override
    public String removePlayer(Integer userId) {
        matchingPool.removePlayer(userId);
        return "remove successfully";
    }
}
