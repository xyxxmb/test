package com.buptyouth.service.impl;

import com.buptyouth.mybatis.dao.ApplyMapper;
import com.buptyouth.mybatis.dao.DeviceMapper;
import com.buptyouth.mybatis.dao.UserMapper;
import com.buptyouth.mybatis.model.Apply;
import com.buptyouth.mybatis.model.ApplyExample;
import com.buptyouth.mybatis.model.User;
import com.buptyouth.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private ApplyMapper applyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public String[][] getDeviceByDate(int target_type, int target_id, String date_str) throws ParseException {
        //将指定日期从String转换成Date类型
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        Date date=format.parse(date_str);

        //计算一天后的日期
        Date date_plus = new Date(date.getTime() + (long)1 * 24 * 60 * 60 * 1000);

        /*System.out.println(date);
        System.out.println(date_plus);*/

        /*SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
        String time=df.format(date);*/

        //按申请类型，申请场地或设备的ID，申请开始日期和结束日期在指定日期内，筛选apply表格，并按开始时间升序排列
        ApplyExample applyExample = new ApplyExample();
        applyExample.createCriteria()
                .andTargetTypeEqualTo(target_type).andTargetIdEqualTo(target_id)
                .andStartTimeGreaterThanOrEqualTo(date).andEndTimeLessThanOrEqualTo(date_plus);
        /*.andCreateTimeGreaterThanOrEqualTo(date).andEndTimeLessThanOrEqualTo(date_plus)*/
        applyExample.setOrderByClause("start_time desc");

        List<Apply> list_apply = applyMapper.selectByExample(applyExample);

        //返回筛选得到的申请表中，各个申请表的开始和结束时间
        int len_apply = list_apply.size();
        String [][] start_end_time = new String[len_apply][2];
        /*//若剩余数量大于0，则返回空
        Device device = deviceMapper.selectByPrimaryKey(target_id);
        int total_number = device.getTotalNumber();
        if (rest_number > 0)
            return start_end_time;*/
        //否则返回占用时间段
        for(int i = 0; i<list_apply.size(); i++)
        {
            Apply apply = list_apply.get(i);
            SimpleDateFormat df=new SimpleDateFormat("HH:mm");
            String start_time=df.format(apply.getStartTime());
            String end_time = df.format(apply.getEndTime());
            start_end_time[i][0] = start_time;
            start_end_time[i][1] = end_time;
        }

        return start_end_time;
    }

    @Override
    public int deviceApply(String user_id, int applicant_type, String activity_name, int people_number,
                         String date_str, String start_HH_mm, String end_HH_mm,
                         int target_type, int target_id, int use_media) throws ParseException {
        Apply apply = new Apply();
        apply.setUserId(user_id);
        apply.setApplicantType(applicant_type);
        User user = userMapper.selectByPrimaryKey(user_id);
        apply.setUserId(user.getUserId());
        apply.setActivityName(activity_name);
        apply.setPeopleNumber(people_number);
        apply.setTargetType(target_type);
        apply.setTargetId(target_id);
        //target_number
        if(target_type == 0)
        {
            apply.setUseMedia(use_media);
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date start_time = format.parse(date_str+ " " + start_HH_mm);
        apply.setStartTime(start_time);
        Date end_time = format.parse(date_str + " " + end_HH_mm);
        apply.setEndTime(end_time);
        Date d = new Date();
        apply.setCreateTime(d);
        apply.setCollege(user.getCollege());
        apply.setDepartment(user.getDepartment());
        //target_auditor
        //reason
        if(applicant_type == 3)//教师
        {
            apply.setState(1);//一级审核通过
        }
        else
            apply.setState(0);//待审核
        return applyMapper.insertSelective(apply);

    }
}
