package com.happytogether.contacts;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
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

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Monsterkill on 2018/5/9.
 */

public class RemindPerson extends AppCompatActivity {
    EditText title, remark;
    TextView date, time;
    Button button;
    int year;

    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.remind_person);


        title = (EditText) findViewById(R.id.remind_title);
        remark = (EditText) findViewById(R.id.remind_remark);

        date = (TextView) findViewById(R.id.remind_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RemindPerson.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日");

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
                        time.setText(hourOfDay + "时" + minute + "分");
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
            }
        });

        button = (Button) findViewById(R.id.remind_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEvent();
            }
        });
    }

    public void setEvent() {
        //先定义一个URL，到时作为调用系统日历的uri的参数
        String calanderRemiderURL = "content://com.android.calendar/reminders";

        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2015, 6, 23, 7, 30);    //注意，月份的下标是从0开始的
        startMillis = beginTime.getTimeInMillis();    //插入日历时要取毫秒计时
        Calendar endTime = Calendar.getInstance();
        endTime.set(2015, 6, 23, 10, 30);
        endMillis = endTime.getTimeInMillis();

        ContentValues eValues = new ContentValues();  //插入事件
        ContentValues rValues = new ContentValues();  //插入提醒，与事件配合起来才有效
        TimeZone tz = TimeZone.getDefault();//获取默认时区

//插入日程
        eValues.put(CalendarContract.Events.DTSTART, startMillis);
        eValues.put(CalendarContract.Events.DTEND, endMillis);
        eValues.put(CalendarContract.Events.TITLE, "见导师");
        eValues.put(CalendarContract.Events.DESCRIPTION, "去实验室见研究生导师");
        eValues.put(CalendarContract.Events.CALENDAR_ID, calID);
        eValues.put(CalendarContract.Events.EVENT_LOCATION, "计算机学院");
        eValues.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Uri uri = getContentResolver().insert(CalendarContract.Events.CONTENT_URI, eValues);

//插完日程之后必须再插入以下代码段才能实现提醒功能
        String myEventsId = uri.getLastPathSegment(); // 得到当前表的_id
        rValues.put("event_id", myEventsId);
        rValues.put("minutes", 10);	//提前10分钟提醒
        rValues.put("method", 1);	//如果需要有提醒,必须要有这一行
        getContentResolver().insert(Uri.parse(calanderRemiderURL),rValues);
    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }
}
