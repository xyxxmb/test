package com.buptyouth.service;

import java.text.ParseException;
import java.util.List;

public interface AuditorService {
    //获取有待审核申请的日期及对应日期的审核状态
    String [][] getApplyByDate(String user_id, String start_date, String end_date) throws ParseException;//String start_date, String end_date yyyy-MM-dd
    //获取指定时间，各个场地设备在各个时间段下的审核状态   资源类型（设备场地） 资源ID 开始时间 结束时间 审核状态
    List<List<String>> getApplyByTime(String user_id, String target_date) throws ParseException;//target_date为指定日期yyyy-MM-dd
    //获取指定申请的申请详情,返回申请人姓名、活动名称、活动人数、借用开始时间和结束时间、借用场地名称、借用单位、设备借用日期、设备名称
    List<List<String>> getApplyDetail(String user_id, int target_type, int target_id, String start_date, String end_date) throws ParseException;//start_date end_state为指定时间yyyy-MM-dd HH:mm
    //审核人审核，返回是否成功
    int checkApply(String user_id, int apply_id, int check);//check为1为通过
}
