package com.buptyouth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.buptyouth.service.AuditorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/auditor")
public class AuditorController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuditorService auditorService;

    @RequestMapping(value = "/get_apply_by_date", method = RequestMethod.POST)
    private @ResponseBody
    JSONObject get_by_date(@RequestBody HashMap<String, Object> reqData) throws ParseException {
        String user_id = (String) reqData.get("user_id");
        String start_date = (String) reqData.get("start_date");
        String end_date = (String) reqData.get("end_date");
        String[][] re = auditorService.getApplyByDate(user_id, start_date, end_date);
        JSONArray json = new JSONArray();
        for (int i = 0; i < re.length; i++) {
            JSONObject t_json = new JSONObject();
            t_json.put("date", re[i][0]);
            t_json.put("state", re[i][1]);
            String s_i = String.valueOf(re.length-i);
            json.add(t_json);
        }
        JSONObject re_json = new JSONObject();
        re_json.put("getApplyByDate", json);
        return re_json;
    }
    /*{
        "user_id": 1234,
        "start_date": "2018-3-1",
        "end_date":"2018-3-31"
      }*/

    @RequestMapping(value = "/get_apply_by_time", method = RequestMethod.POST)
    private @ResponseBody
    JSONObject getApplyByTime(@RequestBody HashMap<String, Object> reqData) throws ParseException {
        String user_id = (String) reqData.get("user_id");
        String target_date = (String) reqData.get("target_date");
        List<List<String>> re = auditorService.getApplyByTime(user_id, target_date);
        JSONArray json = new JSONArray();
        for (int i = 0; i < re.size(); i++) {
            JSONObject t_json = new JSONObject();
            List t_re = re.get(i);
            t_json.put("target_type", t_re.get(0));
            t_json.put("target_id", t_re.get(1));
            t_json.put("start_time", t_re.get(2));
            t_json.put("end_time", t_re.get(3));
            t_json.put("state", t_re.get(4));
            String s_i = String.valueOf(re.size()-i);
            json.add(t_json);
        }
        JSONObject re_json = new JSONObject();
        re_json.put("getApplyByTime", json);
        return re_json;
    }
    /*{
        "user_id": 1234,
            "target_date": "2018-3-10"
    }*/
    @RequestMapping(value = "/get_apply_detail", method = RequestMethod.POST)
    private @ResponseBody
    JSONObject getApplyDetail(@RequestBody HashMap<String, Object> reqData) throws ParseException {
        String user_id = (String) reqData.get("user_id");
        int target_type = (int) reqData.get("target_type");
        int target_id = (int) reqData.get("target_id");
        String start_date = (String) reqData.get("start_date");
        String end_date = (String) reqData.get("end_date");
        List<List<String>> re = auditorService.getApplyDetail(user_id, target_type, target_id, start_date, end_date);
        JSONArray json = new JSONArray();
        for (int i = 0; i < re.size(); i++) {
            JSONObject t_json = new JSONObject();
            List t_re = re.get(i);
            t_json.put("user_name", t_re.get(0));
            t_json.put("activity_name", t_re.get(1));
            t_json.put("people_number", t_re.get(2));
            t_json.put("start_time", t_re.get(3));
            t_json.put("end_time", t_re.get(4));
            t_json.put("roomordevice_name", t_re.get(5));
            t_json.put("applicant_type", t_re.get(6));
            t_json.put("apply_id", t_re.get(7));
            String s_i = String.valueOf(re.size()-i);
            json.add(t_json);
        }
        JSONObject re_json = new JSONObject();
        re_json.put("getApplyDetail", json);
        return re_json;
    }
    /*{
        "user_id": 123,
            "target_type": 1,
            "target_id":1,
            "start_date":"2018-3-10 12:30",
            "end_date":"2018-3-10 13:00"
    }*/
    @RequestMapping(value = "/check_apply", method = RequestMethod.POST)
    private @ResponseBody
    JSONObject checkApply(@RequestBody HashMap<String, Object> reqData) throws ParseException {
        String user_id = (String) reqData.get("user_id");
        int apply_id = (int) reqData.get("apply_id");
        int check = (int) reqData.get("check");

        JSONObject json = new JSONObject();
        int re = auditorService.checkApply(user_id, apply_id, check);
        if(re == 1)
            json.put(String.valueOf(re) , "success");
        else
            json.put(String.valueOf(re) , "error");
        return json;
    }
    /*{
        "user_id": 123,
            "apply_id": 1,
            "check":1
    }*/

}

