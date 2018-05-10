package com.happytogether.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Monsterkill on 2018/5/9.
 */

public class RemindPerson extends AppCompatActivity {
    EditText title, remark, early;
    TextView date, time;
    Button button;
    String dateStr,timeStr;
    String pattern = "yyyy年MM月dd日 HH时mm分";
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.remind_person);


        title = (EditText) findViewById(R.id.remind_title);
        remark = (EditText) findViewById(R.id.remind_remark);
        early = (EditText) findViewById(R.id.remind_early);
        final int early_day = Integer.parseInt(early.getText().toString());
        date = (TextView) findViewById(R.id.remind_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RemindPerson.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateStr = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                        date.setText(dateStr);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        time = (TextView) findViewById(R.id.remind_time);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(RemindPerson.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeStr = hourOfDay + "时" + minute + "分";
                        time.setText(timeStr);
                        c.set(hourOfDay,minute);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });
        final long datetime = getStringToDate(dateStr + timeStr, pattern);
        button = (Button) findViewById(R.id.remind_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCalendarEvent(RemindPerson.this, title.getText().toString(), remark.getText().toString(), datetime, early_day);
                Toast.makeText(RemindPerson.this,"成功添加日程提醒",Toast.LENGTH_SHORT);
                finish();
            }
        });
    }

    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try{
            date = dateFormat.parse(dateString);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public void setEvent() {
        //先定义一个URL，到时作为调用系统日历的uri的参数
        String calanderRemiderURL = "content://com.android.calendar/reminders";

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 6, 23, 7, 30);    //注意，月份的下标是从0开始的
        startMillis = c.getTimeInMillis();    //插入日历时要取毫秒计时
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 6, 23, 10, 30);
        endMillis = c.getTimeInMillis();

        ContentValues eValues = new ContentValues();  //插入事件
        ContentValues rValues = new ContentValues();  //插入提醒，与事件配合起来才有效
        TimeZone tz = TimeZone.getDefault();//获取默认时区

//插入日程
        eValues.clear();
        eValues.put(CalendarContract.Events.DTSTART, startMillis);
        eValues.put(CalendarContract.Events.DTEND, endMillis);
        eValues.put(CalendarContract.Events.TITLE, String.valueOf(title.getText()));
        eValues.put(CalendarContract.Events.DESCRIPTION, String.valueOf(remark.getText()));
        eValues.put(CalendarContract.Events.CALENDAR_ID, calID);
        eValues.put(CalendarContract.Events.EVENT_LOCATION, "计算机学院");
        eValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());

        @SuppressLint("MissingPermission") Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eValues);
        Log.d("日程提醒","已添加"+title.getText());
        //插完日程之后必须再插入以下代码段才能实现提醒功能
        String myEventsId = uri.getLastPathSegment(); // 得到当前表的_id
        rValues.put("event_id", myEventsId);
        rValues.put("minutes", 10);	//提前10分钟提醒
        rValues.put("method", 1);	//如果需要有提醒,必须要有这一行
        getContentResolver().insert(Uri.parse(calanderRemiderURL),rValues);
    }

    /**
     * 添加日历事件
     */
    public static void addCalendarEvent(Context context, String title, String description, long reminderTime, int previousDate) {
        if (context == null) {
            return;
        }
        int calId = checkCalendarAccount(context); //获取日历账户的id
        if (calId < 0) { //获取账户id失败直接返回，添加日历事件失败
            return;
        }

        //添加日历事件
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(reminderTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
        mCalendar.setTimeInMillis(start + 10 * 60 * 1000);//设置终止时间，开始时间加10分钟
        long end = mCalendar.getTime().getTime();
        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", description);
        event.put("calendar_id", calId); //插入账户的id
        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");//这个是时区，必须有
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDER_EVENT_URL), event); //添加事件
        if (newEvent == null) { //添加日历事件失败直接返回
            return;
        }

        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        values.put(CalendarContract.Reminders.MINUTES, previousDate * 24 * 60);// 提前previousDate天有提醒
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
        if(uri == null) { //添加事件提醒失败直接返回
            return;
        }
    }
    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context) {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDER_URL), null, null, null, null);
        try {
            if (userCursor == null) { //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else {
                return -1;
            }
        } finally {
            if (userCursor != null) {
                userCursor.close();
            }
        }
    }

}
