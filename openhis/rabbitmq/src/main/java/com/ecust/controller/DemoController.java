package com.ecust.controller;

import com.alibaba.fastjson.JSON;
import com.ecust.pojo.Users;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DemoController {
    @GetMapping("/hello")
    @ResponseBody
    public Users getBean(){
//        Users users = new Users("xxx", "xxx");
//        return JSON.toJSONString(users);
        return null;
    }
}
