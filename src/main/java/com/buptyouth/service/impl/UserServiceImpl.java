package com.buptyouth.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.naming.NamingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buptyouth.mybatis.dao.AuditorMapper;
import com.buptyouth.mybatis.dao.MasterMapper;
import com.buptyouth.mybatis.dao.StaffMapper;
import com.buptyouth.mybatis.dao.UndergraduateMapper;
import com.buptyouth.mybatis.dao.UserMapper;
import com.buptyouth.mybatis.dao.UserwechatMapper;
import com.buptyouth.mybatis.model.Auditor;
import com.buptyouth.mybatis.model.AuditorExample;
import com.buptyouth.mybatis.model.LDAPAuthentication;
import com.buptyouth.mybatis.model.Master;
import com.buptyouth.mybatis.model.MasterExample;
import com.buptyouth.mybatis.model.Staff;
import com.buptyouth.mybatis.model.StaffExample;
import com.buptyouth.mybatis.model.Undergraduate;
import com.buptyouth.mybatis.model.UndergraduateExample;
import com.buptyouth.mybatis.model.User;
import com.buptyouth.mybatis.util.DataSourceHolder;
import com.buptyouth.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuditorMapper auditorMapper;

    @Resource
    private UndergraduateMapper undergraduateMapper;

    @Resource
    private MasterMapper masterMapper;

    @Resource
    private StaffMapper staffMapper;

    @Resource
    private UserwechatMapper wechatMapper;

    @Override
    public int wechat_login(String userid, String password) throws NamingException {
        LDAPAuthentication ldapAuthentication = new LDAPAuthentication();
        String username = ldapAuthentication.authenricate(String.valueOf(userid), password);
        if (username == null)
            return 0;
        else
            return 1;
    };

    @Override
    public int web_login(String userid, String password) throws NamingException {
        LDAPAuthentication ldapAuthentication = new LDAPAuthentication();
        String username = ldapAuthentication.authenricate(String.valueOf(userid), password);
        if (username == null)
            return 0; //密码错误或学号不匹配
        else {
            AuditorExample auditorExample = new AuditorExample();
            auditorExample.createCriteria().andAuditorIdEqualTo(String.valueOf(userid));
            /* System.out.println(auditorExample); */
            List<Auditor> se_auditor = auditorMapper.selectByExample(auditorExample);
            if (se_auditor == null || se_auditor.size() <= 0)
                return -1; //非审核人
            else
                return 1;
        }
    }

    @Override
    public List<Undergraduate> getUndergraduateInfo(String userid) {
        // 在远程数据库中查找本科生信息
        UndergraduateExample undergraduateExample = new UndergraduateExample();
        undergraduateExample.createCriteria().andSchoolNumberEqualTo(userid);
        return undergraduateMapper.selectByExample(undergraduateExample);
    }

    @Override
    public List<Master> getMasterInfo(String userid) {
        // 在远程数据库中查找研究生信息
        MasterExample masterExample = new MasterExample();
        masterExample.createCriteria().andSchoolNumberEqualTo(userid);
        return masterMapper.selectByExample(masterExample);
    }

    @Override
    public List<Staff> getStaffInfo(String userid) {
        // 在远程数据库中查找教职工信息
        StaffExample staffExample = new StaffExample();
        staffExample.createCriteria().andStaffNumberEqualTo(userid);
        return staffMapper.selectByExample(staffExample);
    }

    @Override
    public boolean isExistUser(String userid) {
        if (userMapper.selectByPrimaryKey(userid) != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    // 绑定用户信息
    public boolean bindUserInfo(String userid) {

        String username = "";
        String college = "";
        String department = "";

        // 连接到远程数据库
        DataSourceHolder.setDataSources("hikari2DataSource");
        DataSourceHolder.getDataSources();

        // 本科生中查找
        List<Undergraduate> undergraduate = getUndergraduateInfo(userid);
        if (!undergraduate.isEmpty()) {
            username = undergraduate.get(0).getXmpy(); // 姓名拼音（姓名会乱码GB2312，转码也无效）
            college = undergraduate.get(0).getCollegeCode(); // 学院代码
            department = undergraduate.get(0).getZydm(); // 专业代码
        }
        // 研究生中查找
        List<Master> master = getMasterInfo(userid);
        if (!master.isEmpty()) {
            username = master.get(0).getXmpy(); // 姓名拼音（姓名会乱码GB2312，转码也无效）
            college = master.get(0).getCollegeCode(); // 学院
            department = master.get(0).getRxzydm(); // 入学专业代码
        }
        // 教职工中查找
        List<Staff> staff = getStaffInfo(userid);
        if (!staff.isEmpty()) {
            username = staff.get(0).getXmpy(); // 姓名拼音（姓名会乱码GB2312，转码也无效）
        }

        // 切回到本地数据库
        DataSourceHolder.setDataSources("hikariDataSource");
        DataSourceHolder.getDataSources();

        // user表插入：不用插入password和user_type
        User user = new User();
        user.setUserId(userid);
        user.setUserName(username);
        user.setCollege(college);
        user.setDepartment(department);

        int userRet = userMapper.insertSelective(user);

        if (userRet == 1) { // 插入成功
            return true;
        } else {
            return false;
        }
    }

}
