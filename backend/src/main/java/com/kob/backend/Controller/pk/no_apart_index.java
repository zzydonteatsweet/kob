package com.kob.backend.Controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pk/")
public class no_apart_index {
    @RequestMapping("index/")
    public String index() {
        return "/pk/index.html" ;
    }
}
