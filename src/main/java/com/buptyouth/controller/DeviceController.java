package com.buptyouth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.buptyouth.service.DeviceService;
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

@Controller
@RequestMapping("/device")
public class DeviceController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/get_by_date", method = RequestMethod.POST)
    private @ResponseBody
    JSONObject get_by_date(@RequestBody HashMap<String, Object> reqData) throws ParseException {
        int target_type = (int) reqData.get("target_type");
        int target_id = (int) reqData.get("target_id");
        String date_str = (String) reqData.get("date_str");
        String[][] re = deviceService.getDeviceByDate(target_type, target_id, date_str);
        JSONArray json = new JSONArray();
        for (int i = 0; i < re.length; i++) {
            JSONObject t_json = new JSONObject();
            t_json.put("start_time", re[i][0]);
            t_json.put("end_time", re[i][1]);
            String s_i = String.valueOf(re.length-i);
            json.add(t_json);
        }
        JSONObject re_json = new JSONObject();
        re_json.put("getByDate", json);
        return re_json;
    }
    /*{
        "target_type": 1,
            "target_id": 1,
            "date_str":"2018-3-10"
    }*/

    @RequestMapping(value = "/device_apply", method = RequestMethod.POST)
    private @ResponseBody JSONObject deviceApply(@RequestBody HashMap<String, Object> reqData) throws ParseException {
        String user_id = (String) reqData.get("user_id");
        int applicant_type = (int) reqData.get("applicant_type");
        String activity_name = (String) reqData.get("activity_name");
        int people_number = (int) reqData.get("people_number");
        String date_str = (String) reqData.get("date_str");
        String start_HH_mm = (String) reqData.get("start_HH_mm");
        String end_HH_mm = (String) reqData.get("end_HH_mm");
        int target_type = (int) reqData.get("target_type");
        int target_id = (int) reqData.get("target_id");
        int use_media = (int) reqData.get("use_media");
        int re = deviceService.deviceApply(user_id, applicant_type, activity_name, people_number,
                date_str, start_HH_mm, end_HH_mm, target_type, target_id, use_media);
        JSONObject json = new JSONObject();
        String s_re = String.valueOf(re);
        if(re == 1)
            json.put(s_re , "success");
        else
            json.put(s_re , "error");
        return json;
    }
    /*{
        "user_id":12,
            "applicant_type":3,
            "activity_name":"aaaaa",
            "people_number":4,
            "date_str":"2018-3-1",
            "start_HH_mm":"10:00",
            "end_HH_mm":"11:00",
            "target_type": 1,
            "target_id": 1,
            "use_media":1
    }*/
}
