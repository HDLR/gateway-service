package com.dfjinxin.gateway.service.test;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test/get")
    public String test(){
        return "test success";
    }
}
