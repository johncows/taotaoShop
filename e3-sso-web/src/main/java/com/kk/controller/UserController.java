package com.kk.controller;

import com.kk.pojo.User;
import com.kk.sso.UserService;
import com.kk.utils.E3Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserController {


   @Autowired
    private UserService userService;

   // 根据参数进行合法性校验
    @SuppressWarnings("AlibabaCommentsMustBeJavadocFormat")
    @RequestMapping("check/{value}/{type}")
    @ResponseBody
    public E3Result checkParameter(@PathVariable  String value,@PathVariable Integer type){
        return userService.checkData(value,type);
    }


    @ResponseBody
    @RequestMapping("register")
    public E3Result userRegister(User user){
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(password);
        return userService.register(user);
    }



}