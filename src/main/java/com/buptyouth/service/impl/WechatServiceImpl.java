package com.buptyouth.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.buptyouth.mybatis.dao.UserMapper;
import com.buptyouth.mybatis.dao.UserwechatMapper;
import com.buptyouth.mybatis.model.User;
import com.buptyouth.mybatis.model.Userwechat;
import com.buptyouth.mybatis.model.UserwechatExample;
import com.buptyouth.service.WechatService;
import com.buptyouth.util.HttpClientUtil;
import com.buptyouth.util.HttpClientUtil.HttpClientResponse;

import net.sf.json.JSONObject;

@Service("wechatService")
public class WechatServiceImpl implements WechatService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserwechatMapper wechatMapper;

    @Override
    public List<Userwechat> getUserWechatByCode(String code) {
        UserwechatExample wechatExample = new UserwechatExample();
        wechatExample.createCriteria().andCodeEqualTo(code);
        return wechatMapper.selectByExample(wechatExample);
    }

    @Override
    public List<Userwechat> getUserWechatByOpenid(String openid) {
        UserwechatExample wechatExample = new UserwechatExample();
        wechatExample.createCriteria().andOpenIdEqualTo(openid);
        return wechatMapper.selectByExample(wechatExample);
    }

    @Override
    public String getOpenIdFromWechatServer(String wechatCode, StringBuilder errorCode) {

        String openId = null;
        String appid = "wxba09f189c8b1b661";
        String appsecret = "eefdcc7f76ccc895c5eb74fe19bdd570";

        HttpClientResponse wechatServerResponse = HttpClientUtil.sendHttpGet(
                "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid + "&secret="
                        + appsecret + "&code="
                        + wechatCode + "&grant_type=authorization_code ");

        if (wechatServerResponse != null) {
            System.err.println("resp for wechatCode " + wechatCode + ": " +
                    wechatServerResponse.getStatusCode() + ":" + wechatServerResponse.getResponseContent());
        } else {
            System.err.println("resp for wechatCode " + wechatCode + ": null");
        }

        if (wechatServerResponse == null || wechatServerResponse.getStatusCode() != HttpStatus.SC_OK) {
            return openId;
        }

        JSONObject responseContent = JSONObject.fromObject(wechatServerResponse.getResponseContent());

        if (responseContent.containsKey("openid")) {
            openId = responseContent.getString("openid");
        } else if (responseContent.containsKey("errcode")) {
            errorCode.append(responseContent.getString("errcode"));
        }

        return openId;
    }

    @Override
    public boolean bindUserWechatInfo(String userid, String code, String openid) {

        User user = userMapper.selectByPrimaryKey(userid);

        // userwechat表插入
        Userwechat userWechat = new Userwechat();
        userWechat.setUserId(user.getUserId());
        userWechat.setCode(code);
        userWechat.setOpenId(openid);
        userWechat.setCollege(user.getCollege());
        userWechat.setDepartment(user.getDepartment());

        int wechatRet = wechatMapper.insertSelective(userWechat);
        if (wechatRet == 1) { // 插入成功
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int updateCode(Userwechat userwechat, String code) {
        userwechat.setCode(code);
        return wechatMapper.updateByPrimaryKey(userwechat);
    }

    // 检查微信登录状态
    @Override
    public boolean wechatLoginStatus(String code, String openid) {
        List<Userwechat> userwechat = getUserWechatByCode(code);
        if (!userwechat.isEmpty()) { // code 存在
            return true;
        }
        userwechat = getUserWechatByOpenid(openid);
        if (!userwechat.isEmpty()) { // openid 存在
            // 更新code
            updateCode(userwechat.get(0), code);
            return true;
        }
        // 如果code和openid都不存在，则说明没有登录过，要执行绑定（插入）操作
        return false;
    }

}
