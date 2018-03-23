package com.buptyouth.service;

import java.util.List;

import com.buptyouth.mybatis.model.Userwechat;

public interface WechatService {

    List<Userwechat> getUserWechatByCode(String code);

    List<Userwechat> getUserWechatByOpenid(String openid);

    boolean bindUserWechatInfo(String userid, String code, String openid);

    int updateCode(Userwechat userwechat, String code);

    boolean wechatLoginStatus(String code, String openid);

    String getOpenIdFromWechatServer(String wechatCode, StringBuilder errorCode); // 通过code获取openid

}
