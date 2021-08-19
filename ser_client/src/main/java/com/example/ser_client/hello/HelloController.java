package com.example.ser_client.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.util.IdUtil;

@Configuration
@Controller
public class HelloController {

    @Value("${server.port}")
    private int port;

    @Autowired
    UserDao userDao;

    @GetMapping("/hello")
    @ResponseBody
    public JSONObject hello(){
        JSONObject res = new JSONObject();
        res.put("server.ip", getIpAddr());
        res.put("server.port", port);
        res.put("message", "hello");
        return  res;
    }

    @PostMapping("/getSomething")
    @ResponseBody
    public JSONObject getSomething(@RequestBody User body){
        JSONObject res = new JSONObject();
        if (!userDao.existsByUserId(body.getUserId())){
            res.put("code", 400);
            res.put("message", "该用户不存在");
            return res;
        }
        body = userDao.getOneByUserId(body.getUserId());
        UserVo userVo = new UserVo();
        userVo.setUserId(body.getUserId());
        userVo.setUsername(body.getUsername());
        userVo.setSomething((double) Math.round((Math.random() * 100 % 2) * 100) / 100);
        userVo.setTotal((double) Math.round((body.getTotal() + userVo.getSomething()) * 100 )/ 100);
        res.put("code", 200);
        res.put("message", userVo);
        body.setTotal(userVo.getTotal());
        userDao.save(body);
        return  res;
    }

    @GetMapping("/getUserList")
    @ResponseBody
    public JSONObject getUserList(){
        JSONObject res = new JSONObject();
        res.put("code", 200);
        res.put("message",  userDao.findAll());
        return res;
    }

    @PostMapping("/addUser")
    @ResponseBody
    public JSONObject addUser(@RequestBody User body){
        JSONObject res = new JSONObject();
        if (userDao.existsByUsername(body.getUsername())){
            res.put("code", 400);
            res.put("message", "用户名已存在");
            return res;
        }
        body.setTotal(0.0);
        body.setUserId(IdUtil.getSnowflake(1, 1).nextId());
        userDao.save(body);
        res.put("code", 200);
        res.put("message", null);
        return  res;
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public JSONObject deleteUser(@RequestBody User body){
        JSONObject res = new JSONObject();
        if (!userDao.existsByUserId(body.getUserId())){
            res.put("code", 400);
            res.put("message", "该用户不存在");
            return res;
        }
        userDao.delete(body);
        res.put("code", 200);
        res.put("message", null);
        return  res;
    }

    public static String getIpAddr(){
        try{
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()){
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip != null
                            && ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && ip.getHostAddress().indexOf(":")==-1){
                        //System.out.println("本机的IP = " + ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
