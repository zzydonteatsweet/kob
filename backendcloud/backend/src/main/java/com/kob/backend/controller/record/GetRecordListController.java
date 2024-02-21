package com.kob.backend.controller.record;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.record.GetRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRecordListController {
    @Autowired
    private GetRecordService getRecordService;

    @GetMapping("/api/record/getlist")
    JSONObject getList(@RequestParam Map<String,String> data) {
        String page = data.get("page");
        return getRecordService.getList(Integer.parseInt(page));
    }
}
