package com.kob.backend.controller.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class GetRankListController {
    @Autowired
    GetRankListService getRankListService;

    @GetMapping("/ranklist/getlist")
    public JSONObject getRankList(@RequestParam MultiValueMap<String,String> data) {
        Integer page = Integer.parseInt(Objects.requireNonNull(data.getFirst("page")));
        return getRankListService.getRankList(page);
    }
}
