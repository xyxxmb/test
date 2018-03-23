package com.buptyouth.service;

import java.text.ParseException;

public interface RoomService {
    String[][] getRoomByDate(int target_type, int target_id, String date_str) throws ParseException;//date_str yyyy-MM-dd
    int roomApply(String user_id, int applicant_type, String activity_name, int people_number,
                  String date_str, String start_HH_mm, String end_HH_mm,
                  int target_type, int target_id, int use_media) throws ParseException;//date_str 的格式为 yyyy-MM-dd,start_HH_mm和end_HH_mm的格式为HH:mm
}
