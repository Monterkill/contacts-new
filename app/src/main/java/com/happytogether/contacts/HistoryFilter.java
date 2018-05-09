package com.happytogether.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.happytogether.framework.resouce_manager.ResourceManager;
import com.happytogether.framework.type.CallRecord;

import java.util.ArrayList;
import java.util.List;

public class HistoryFilter extends AppCompatActivity {
    private ListView mlistview;
    private EditText meditText;
    private ListHistoryAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_filter);
        mlistview = (ListView) findViewById(R.id.listview);
        meditText = (EditText) findViewById(R.id.editText);
        //
        mListAdapter = new ListHistoryAdapter(this, initData());
        mlistview.setAdapter(mListAdapter);


        //meditText监听文本变化
        meditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //告诉adapter 文本有变化了
                mListAdapter.changeText(charSequence.toString());
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
}
