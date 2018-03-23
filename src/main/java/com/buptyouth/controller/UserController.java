package com.buptyouth.controller;

import java.util.HashMap;

import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.buptyouth.service.UserService;
import com.buptyouth.service.WechatService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private WechatService wechatService;

    @RequestMapping(value = "/wechat_login", method = RequestMethod.POST)
    private @ResponseBody JSONObject wechat_login(@RequestBody HashMap<String, Object> reqData) throws NamingException {
        JSONObject json = new JSONObject();
        String code = (String) reqData.get("code");
        // 先通过code检查用户是否已经存在数据库中
        StringBuilder errorcode = new StringBuilder();
        String openid = wechatService.getOpenIdFromWechatServer(code, errorcode); // 根据code计算openid
        if (openid == null) { // 传入的code值错误或者过期，无法解析到openid
            json.put("errorcode: ", errorcode);
            json.put("msg", "传入的code无法解析出openid或code已经过期");
            return json;
        }
        // System.out.println("解析出的 openid：" + openid);
        boolean isLoginByWechat = wechatService.wechatLoginStatus(code, openid); // 检查code和openid是否存在
        if (isLoginByWechat) {
            json.put("msg", "已经通过微信登录，不用绑定");
            return json;
        }
        // 没有通过微信登录过，对userid和password进行鉴权
        String userid = (String) reqData.get("userid");
        String password = (String) reqData.get("password");
        int re = userService.wechat_login(userid, password);
        if (re == 1) {
            // 插入到两个表都成功了才算登录成功
            if (userService.bindUserInfo(userid) && wechatService.bindUserWechatInfo(userid, code, openid)) {
                json.put("msg", "登录成功");
            } else {
                json.put("msg", "登录失败");
            }
        } else
            json.put("msg", "鉴权失败，学号或密码输入错误");
        return json;
    }

    @RequestMapping(value = "/web_login", method = RequestMethod.POST)
    private @ResponseBody JSONObject web_login(@RequestBody HashMap<String, Object> reqData) throws NamingException {
        String userid = (String) reqData.get("userid");
        String password = (String) reqData.get("password");
        int re = userService.web_login(userid, password);
        JSONObject json = new JSONObject();
        if (re == 1) {
            json.put("msg", "审核人登录成功");
        } else if (re == 0)
            json.put("msg", "鉴权失败，学号或密码输入错误");
        else
            json.put("msg", "审核人不存在");
        return json;
    }

}
