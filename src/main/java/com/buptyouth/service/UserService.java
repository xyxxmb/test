package com.buptyouth.service;

import java.util.List;

import javax.naming.NamingException;

import com.buptyouth.mybatis.model.Master;
import com.buptyouth.mybatis.model.Staff;
import com.buptyouth.mybatis.model.Undergraduate;

public interface UserService {

    int wechat_login(String userid, String password) throws NamingException;

    int web_login(String userid, String password) throws NamingException;

    List<Undergraduate> getUndergraduateInfo(String userid);

    List<Master> getMasterInfo(String userid);

    List<Staff> getStaffInfo(String userId);

    boolean bindUserInfo(String userid);

    boolean isExistUser(String userid);

}
