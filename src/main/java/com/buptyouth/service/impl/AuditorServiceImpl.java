package com.buptyouth.service.impl;

import com.buptyouth.mybatis.dao.ApplyMapper;
import com.buptyouth.mybatis.dao.DeviceMapper;
import com.buptyouth.mybatis.dao.RoomMapper;
import com.buptyouth.mybatis.dao.UserMapper;
import com.buptyouth.mybatis.model.*;
import com.buptyouth.service.AuditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class AuditorServiceImpl implements AuditorService {
    @Autowired
    private ApplyMapper applyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public String[][] getApplyByDate(String user_id, String start_date, String end_date) throws ParseException {
        User user = userMapper.selectByPrimaryKey(user_id);
        int user_type = user.getUserType();
        String college = user.getCollege();
        String department = user.getDepartment();

        /*System.out.println(user_type);
        System.out.println(college);
        System.out.println(department);*/

        //根据用户类型选择对应的申请人类型,
        // user_type == 1对应applicant_type为0/1，user_type == 2对应applicant_type为2，user_type == 3对应applicant_type为0/1/2/3

        //将指定日期从String转换成Date类型
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        Date start_time = format.parse(start_date);
        Date t_end_time = format.parse(end_date);
        Date end_time = new Date(t_end_time.getTime() + (long)1 * 24 * 60 * 60 * 1000);//计算一天后的日期

        ApplyExample applyExample = new ApplyExample();
        if(user_type == 1)
        {
            applyExample.createCriteria().andApplicantTypeIn(Arrays.asList(0,1)).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andStartTimeBetween(start_time, end_time).andStateEqualTo(0);
        }
        else if(user_type == 2)
        {
            applyExample.createCriteria().andApplicantTypeEqualTo(2).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andStartTimeBetween(start_time, end_time).andStateEqualTo(0);
        }
        else if(user_type == 3)
        {
            applyExample.createCriteria().andApplicantTypeIn(Arrays.asList(0,1,2,3)).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andStartTimeBetween(start_time, end_time).andStateEqualTo(1);
        }
        applyExample.setOrderByClause("start_time desc");
        List<Apply> list_apply = applyMapper.selectByExample(applyExample);
        System.out.println(list_apply.size());

        String [][] time_state = new String[list_apply.size()][2];
        /*//若剩余数量大于0，则返回空
        Device device = deviceMapper.selectByPrimaryKey(target_id);
        int total_number = device.getTotalNumber();
        if (rest_number > 0)
            return start_end_time;*/
        //否则返回占用时间段
        for(int i = 0; i<list_apply.size(); i++)
        {
            Apply apply = list_apply.get(i);
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            String re_time=df.format(apply.getStartTime());
            int state = apply.getState();
            time_state[i][0] = re_time;
            String s_re = String.valueOf(state);
            time_state[i][1] = s_re;
            System.out.println(re_time);
            System.out.println(s_re);
        }
        return time_state;
    }

    @Override
    public List<List<String>> getApplyByTime(String user_id, String target_date) throws ParseException {
        User user = userMapper.selectByPrimaryKey(user_id);
        int user_type = user.getUserType();
        String college = user.getCollege();
        String department = user.getDepartment();

        /*System.out.println(user_type);
        System.out.println(college);
        System.out.println(department);*/

        //根据用户类型选择对应的申请人类型,
        // user_type == 1对应applicant_type为0/1，user_type == 2对应applicant_type为2，user_type == 3对应applicant_type为0/1/2/3

        //将指定日期从String转换成Date类型
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(target_date);
        Date date_plus = new Date(date.getTime() + (long)1 * 24 * 60 * 60 * 1000);//计算一天后的日期

        ApplyExample applyExample = new ApplyExample();
        if(user_type == 1)
        {
            applyExample.createCriteria().andApplicantTypeIn(Arrays.asList(0,1)).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andStartTimeBetween(date, date_plus);
        }
        else if(user_type == 2)
        {
            applyExample.createCriteria().andApplicantTypeEqualTo(2).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andStartTimeBetween(date, date_plus);
        }
        else if(user_type == 3)
        {
            applyExample.createCriteria().andApplicantTypeIn(Arrays.asList(0,1,2,3)).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andStartTimeBetween(date, date_plus);
        }
        applyExample.setOrderByClause("start_time desc");
        List<Apply> list_apply = applyMapper.selectByExample(applyExample);

        List<List<String>> re_list = new ArrayList<List<String>>();
        /*//若剩余数量大于0，则返回空
        Device device = deviceMapper.selectByPrimaryKey(target_id);
        int total_number = device.getTotalNumber();
        if (rest_number > 0)
            return start_end_time;*/
        /*System.out.println(list_apply.size());*/
        //否则返回占用时间段
        for(int i = 0; i<list_apply.size(); i++)
        {
            Apply apply = list_apply.get(i);
            //资源类型（设备场地） 资源ID 开始时间 结束时间 审核状态
            int target_type = apply.getTargetType();
            int target_id = apply.getTargetId();
            SimpleDateFormat df=new SimpleDateFormat("HH:mm");
            String re_start=df.format(apply.getStartTime());
            String re_end = df.format(apply.getEndTime());
            int re_state = apply.getState();

            List t_list = new ArrayList();
            t_list.add(String.valueOf(target_type));
            t_list.add(String.valueOf(target_id));
            t_list.add(re_start);
            t_list.add(re_end);
            t_list.add(String.valueOf(re_state));

            re_list.add(t_list);
        }
        return re_list;
    }

    @Override
    public List<List<String>> getApplyDetail(String user_id, int target_type, int target_id, String start_date, String end_date) throws ParseException {
        User user = userMapper.selectByPrimaryKey(user_id);
        String user_name = user.getUserName();
        int user_type = user.getUserType();
        String college = user.getCollege();
        String department = user.getDepartment();

        /*System.out.println(user_type);
        System.out.println(college);
        System.out.println(department);*/

        //根据用户类型选择对应的申请人类型,
        // user_type == 1对应applicant_type为0/1，user_type == 2对应applicant_type为2，user_type == 3对应applicant_type为0/1/2/3

        //将指定日期从String转换成Date类型
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date start_time = format.parse(start_date);
        Date end_time = format.parse(end_date);

        ApplyExample applyExample = new ApplyExample();
        if(user_type == 1)
        {
            applyExample.createCriteria().andApplicantTypeIn(Arrays.asList(0,1)).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andTargetTypeEqualTo(target_type).andTargetIdEqualTo(target_id)
                    .andStartTimeLessThanOrEqualTo(start_time).andEndTimeGreaterThanOrEqualTo(end_time);
        }
        else if(user_type == 2)
        {
            applyExample.createCriteria().andApplicantTypeEqualTo(2).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andTargetTypeEqualTo(target_type).andTargetIdEqualTo(target_id)
                    .andStartTimeLessThanOrEqualTo(start_time).andEndTimeGreaterThanOrEqualTo(end_time);
        }
        else if(user_type == 3)
        {
            applyExample.createCriteria().andApplicantTypeIn(Arrays.asList(0,1,2,3)).andCollegeEqualTo(college).andDepartmentEqualTo(department)
                    .andTargetTypeEqualTo(target_type).andTargetIdEqualTo(target_id)
                    .andStartTimeLessThanOrEqualTo(start_time).andEndTimeGreaterThanOrEqualTo(end_time);
        }
        applyExample.setOrderByClause("start_time desc");
        List<Apply> list_apply = applyMapper.selectByExample(applyExample);

        List<List<String>> re_list = new ArrayList<List<String>>();
        /*//若剩余数量大于0，则返回空
        Device device = deviceMapper.selectByPrimaryKey(target_id);
        int total_number = device.getTotalNumber();
        if (rest_number > 0)
            return start_end_time;*/
        //否则返回占用时间段
        for(int i = 0; i<list_apply.size(); i++)
        {
            Apply apply = list_apply.get(i);
            //返回申请人姓名、活动名称、活动人数、借用开始时间和结束时间、借用场地名称、借用单位、设备借用日期、设备名称

            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String re_start=df.format(apply.getStartTime());
            String re_end = df.format(apply.getEndTime());

            List t_list = new ArrayList();
            t_list.add(user_name);
            t_list.add(apply.getActivityName());
            t_list.add(String.valueOf(apply.getPeopleNumber()));
            t_list.add(re_start);
            t_list.add(re_end);
            if(target_type == 0)
            {
                Room room = roomMapper.selectByPrimaryKey(target_id);
                t_list.add(room.getRoomName());
            }
            else
            {
                Device device = deviceMapper.selectByPrimaryKey(target_id);
                t_list.add(device.getDeviceName());
            }
            t_list.add(String.valueOf(apply.getApplicantType()));
            t_list.add(String.valueOf(apply.getApplyId()));

            re_list.add(t_list);
        }
        return re_list;
    }

    @Override
    public int checkApply(String user_id, int apply_id, int check){
        User user = userMapper.selectByPrimaryKey(user_id);
        int user_type = user.getUserType();

        Apply apply = applyMapper.selectByPrimaryKey(apply_id);
        int state = apply.getState();
        if(check == 1)
        {
            if(state == 0)
            {
                state = 1;
            }
            else if(state == 1)
            {
                state = 2;
            }
            else
                return -1;
        }
        else
        {
            if(state == 0)
                state = 3;
            else if (state == 1)
                state = 4;
            else
                return -1;
        }
        apply.setState(state);
        return  applyMapper.updateByPrimaryKey(apply);
    }
}
