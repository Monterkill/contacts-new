package com.happytogether.contacts.util;

import android.content.Context;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.happytogether.framework.log.LogBus;
import com.happytogether.framework.type.CallRecord;
import com.happytogether.framework.type.Contacts;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//工具方法在这里实现
public class MyUtil {

    public static long nowTs(){
        return 888888;
    }

    /*直接用jpinyin了
    public static String converToPinyin(String chinese){
        if(chinese.equals("王维川")){
            return "wangweichuan";
        }
        if(chinese.equals("王博伦")){
            return "wangbolun";
        }
        return "";
    }
    */

    public static boolean callTo(String number){
        LogBus.Log(LogBus.DEBUGTAGS, "calling to " + number);
        return true;
    }

    public static List <CallRecord> filterCallRecordByTime(List <CallRecord> callRecords, long start_ts, long end_ts) {
        Iterator it = callRecords.iterator();
        List <CallRecord> reRecord = new ArrayList<CallRecord>();
        while(it.hasNext())
        {
            CallRecord theRecord = (CallRecord)it.next();
            long startTime = theRecord.getStartTime();
            long endTime = startTime + theRecord.getDuration();
            if(startTime >= start_ts && endTime <= end_ts)
                reRecord.add(theRecord);
        }
        return reRecord;
    }

    public static Integer sumCallRecordsDuration(List callRecords) {
        Iterator it = callRecords.iterator();
        Integer count = 0;
        while(it.hasNext())
        {
            CallRecord theRecord = (CallRecord)it.next();
            count += theRecord.getDuration();
        }
        return count;
    }

    public static List filterCallRecordByKeyWords(List callRecords, String num) throws PinyinException {
        Iterator it = callRecords.iterator();
        List <CallRecord> reRecord = new ArrayList<CallRecord>();
        num = num.toLowerCase(); //为了模糊匹配规定所有字母全部转为小写进行
        while(it.hasNext())
        {
            CallRecord theRecord = (CallRecord)it.next();
            String pattern = "\\w*" + num + "\\w*";
            String content = theRecord.getNumber();
            boolean isMatch = Pattern.matches(pattern, content);
            if(isMatch) {
                reRecord.add(theRecord);
                continue;
            }
            content = theRecord.getName().toLowerCase();

            String wholepinyin = PinyinHelper.convertToPinyinString(content, "", PinyinFormat.WITHOUT_TONE);
            isMatch = Pattern.matches(pattern, wholepinyin);
            if(isMatch) {
                reRecord.add(theRecord);
                continue;
            }

            String initialpinyin = PinyinHelper.getShortPinyin(content);
            isMatch = Pattern.matches(pattern, initialpinyin);
            if(isMatch) {
                reRecord.add(theRecord);
                continue;
            }
        }
        return reRecord;
    }

    public static String formatTs(long ts) {
        long min = (ts % 3600) / 60;
        long sec = ts % 60;
        long hour = ts / 3600;
        String callDurationStr = "";
        if(hour > 0){
            callDurationStr += (hour + "小时");
        }
        if(min > 0){
            callDurationStr += (min + "分钟");
        }
        if (sec >= 0) {
                callDurationStr += (sec + "秒");
        }
        return callDurationStr;
    }

    public static String getCallStatusStr(int code){
        String str = "";
        switch (code){
            case CallRecord.ERROR_NUM: str = "空号"; break;
            case CallRecord.INCOMMING: str = "呼入"; break;
            case CallRecord.OUTGOING: str = "呼出"; break;
            case CallRecord.IN_MISSED: str = "响铃"; break;
            case CallRecord.OUT_MISSED: str = "未接通"; break;
            default:break;
        }
        return str;
    }

    //拨打时间
    public static String formatCallDate(long ts){
        Date tsDate = new Date(ts * 1000);
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date_today = simpleDateFormat.format(today);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String callDateStr = sdf.format(tsDate);
        if (callDateStr.equals(date_today)) { //判断是否为今天
            sdf = new SimpleDateFormat("HH:mm");
            callDateStr = sdf.format(tsDate);
        } else if (date_today.contains(callDateStr.substring(0, 7))) { //判断是否为当月
            sdf = new SimpleDateFormat("dd");
            int callDay = Integer.valueOf(sdf.format(tsDate));

            int day = Integer.valueOf(sdf.format(today));
            if (day - callDay == 1) {
                sdf = new SimpleDateFormat("HH:mm");
                callDateStr = "昨天 "+ sdf.format(tsDate);
            } else {
                sdf = new SimpleDateFormat("MM-dd");
                callDateStr = sdf.format(tsDate);
            }
        } else if (date_today.contains(callDateStr.substring(0, 4))) { //判断是否为当年
            sdf = new SimpleDateFormat("MM-dd");
            callDateStr = sdf.format(tsDate);
        }
        return callDateStr;
    }

}
