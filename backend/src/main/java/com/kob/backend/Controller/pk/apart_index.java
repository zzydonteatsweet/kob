package com.kob.backend.Controller.pk;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/botInfo/")

public class apart_index {
    @RequestMapping("/index/")

    public Map<String,String> index() {
        Map<String,String> p = new TreeMap<String,String>() ;
        p.put("rating","1500") ;
        p.put("name","tiger") ;
        return p ;
    }
}
