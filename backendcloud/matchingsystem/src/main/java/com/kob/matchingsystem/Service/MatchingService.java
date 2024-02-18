package com.kob.matchingsystem.Service;

public interface MatchingService {
    String addPlayer(Integer userId, Integer rating, Integer botId) ;
    String removePlayer(Integer userId) ;

}
