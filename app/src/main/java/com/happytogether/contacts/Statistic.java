package com.happytogether.contacts;

/**
 * Created by Monsterkill on 2018/5/9.
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.happytogether.contacts.task.QueryCallDurationByTimeTask;
import com.happytogether.framework.log.LogBus;
import com.happytogether.framework.processor.Processor;
import com.happytogether.framework.task.Task;

public class Statistic extends AppCompatActivity{
    private TextView start_date,end_date, count_all, count_out;
    private long SStime,EEndtime;
    private Button button;
    @Override
    protected void onCreate (Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.statistic);

        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);
        count_all = (TextView) findViewById(R.id.count_all);
        count_out = (TextView) findViewById(R.id.count_out);
        start_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                showDatePickerDialog(start_date);
            }
        });

        String Start_time="year"+"mouthOfYear"+"dayOfMonth";

        try {
            String Stime=dateToStamp(Start_time);
            SStime = Integer.parseInt(Stime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        end_date.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(end_date);
            }
        });
        String End_time="year"+"mouthOfYear"+"dayOfMonth";
        try {
            String EEnd_time=dateToStamp(End_time);
            EEndtime=Integer.parseInt(EEnd_time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        button = (Button) findViewById(R.id.btn_statistic);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count_all.setText("3次");
                count_out.setText("3次");
            }
        });

//        String record_1=getrecord(SStime,EEndtime);
//        Log.d("text",record_1);
//        Toast.makeText(Statistic.this,record_1,Toast.LENGTH_SHORT).show();


    }

    private void showDatePickerDialog(final TextView textView) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(Statistic.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                textView.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String getrecord(long ss, long ee){

        //new FrameworkInitialization();

        Task test_task = new QueryCallDurationByTimeTask(ss,ee);
        Processor.getInstance().process(test_task);
        String s = "";
        String s1="wrong";
        while(!test_task.finished()){ }//加载完成后再进行下一步

        if(test_task.getStatus() == Task.SUCCESS){

            /*for(CallRecord r : (List<CallRecord>)test_task.getResult()){
                s += (r.getDuration() + "\t");
            }*/
            s += test_task.getResult();

            LogBus.Log(LogBus.DEBUGTAGS, "finish task" + s);
            return s;
        }
        else
        {
            return s1;
        }

    }
}