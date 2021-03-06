package com.kk.controller;

import com.kk.pojo.User;
import com.kk.sso.UserService;
import com.kk.utils.CookieUtils;
import com.kk.utils.E3Result;
import com.kk.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("user")
public class UserController {


   @Autowired
    private UserService userService;

   // 根据参数进行合法性校验
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

    @ResponseBody
    @RequestMapping("login")
    public E3Result login(User user, HttpServletRequest request, HttpServletResponse response){
        E3Result e3Result = userService.checkUserAccount(user);
        if(e3Result.getStatus()!=200){
            return e3Result;
        }
        String tokenKey = (String) e3Result.getData();
//        利用工具类将cookie写入浏览器
        CookieUtils.setCookie(request,response,"e3-token",tokenKey);
        return e3Result;
    }

    @ResponseBody
    @RequestMapping(value = "token/{value}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String isLogin(@PathVariable String value,String callback){
        E3Result e3Result = userService.checkUserInfo(value);
        //相应结果前 判断是否由callback请求
        if(StringUtils.isNotBlank(callback)) {
            return callback+"("+ JsonUtils.objectToJson(e3Result)+")";
        }

        return JsonUtils.objectToJson(e3Result);

    }







}
