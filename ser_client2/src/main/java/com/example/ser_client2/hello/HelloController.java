package com.example.ser_client2.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Configuration
@Controller
public class HelloController{

    @Value("${server.port}")
    private int port;

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "server.port:" + port + " Hello China";
    }
}
