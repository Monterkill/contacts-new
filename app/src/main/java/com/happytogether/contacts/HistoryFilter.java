package com.happytogether.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.happytogether.contacts.task.QueryCallRecordByKeyWordsTask;
import com.happytogether.framework.processor.Processor;
import com.happytogether.framework.resouce_manager.ResourceManager;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.CallRecord;

import java.util.ArrayList;
import java.util.List;

public class HistoryFilter extends AppCompatActivity {

    private ListHistoryAdapter adapter;
    private EditText meditText;
    private List<CallRecord> HistoryList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_filter);
        ListView mlistview = (ListView) findViewById(R.id.history_view);
        meditText = (EditText) findViewById(R.id.editText);
        //
        adapter = new ListHistoryAdapter(this, R.layout.item_history, HistoryList);
        mlistview.setAdapter(adapter);


        //meditText监听文本变化
        meditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //告诉adapter 文本有变化了
                //adapter.changeText(charSequence.toString());
                System.out.println(charSequence.toString());
                filterData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private List<CallRecord> initData() {
        List re = ResourceManager.getInstance().getAllCallRecord();
        return re;
    }

    public void filterData(String temp){
        Task task = new QueryCallRecordByKeyWordsTask(temp);
        Processor.getInstance().process(task);
        while(!task.finished());
        HistoryList.clear();
        if(task.getStatus() == Task.SUCCESS){
            HistoryList.addAll((List<CallRecord>)task.getResult());
        }
    }
}
