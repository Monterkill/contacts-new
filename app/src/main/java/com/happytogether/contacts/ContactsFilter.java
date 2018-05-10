package com.happytogether.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.happytogether.contacts.task.QueryAllCallRecordTask;
import com.happytogether.contacts.task.QueryAllContactsTask;
import com.happytogether.contacts.task.QueryCallRecordByKeyWordsTask;
import com.happytogether.contacts.task.QueryContactsByKeyWordsTask;
import com.happytogether.framework.processor.Processor;
import com.happytogether.framework.resouce_manager.ResourceManager;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.CallRecord;
import com.happytogether.framework.type.Contacts;

import java.util.ArrayList;
import java.util.List;

public class ContactsFilter extends AppCompatActivity {

    private ListContactsAdapter adapter;
    private EditText meditText;
    private List<Contacts> ContactsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_filter);
        ListView mlistview = findViewById(R.id.contacts_view2);
        meditText = (EditText) findViewById(R.id.editText);
        adapter = new ListContactsAdapter(this, R.layout.item_contacts, ContactsList);
        updateData();
        setListViewHeightBasedOnChildren(mlistview);
        mlistview.setAdapter(adapter);

        //meditText监听文本变化
        meditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //告诉adapter 文本有变化了
                Log.i("chan","1");
                adapter.changeText(charSequence.toString());
                Log.i("chan","1.5");
                System.out.println(charSequence.toString());
                filterData(charSequence.toString());
                Log.i("chan","2");
                System.out.println("len = " + ContactsList.size());
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
        Task task = new QueryContactsByKeyWordsTask(temp);
        Processor.getInstance().process(task);
        while(!task.finished());
        Log.i("chan","1.8");
        ContactsList.clear();
        if(task.getStatus() == Task.SUCCESS){
            ContactsList.addAll((List<Contacts>)task.getResult());
        }
        adapter.notifyDataSetChanged();
    }

    public void updateData(){
        Task task = new QueryAllContactsTask();
        Processor.getInstance().process(task);
        while(!task.finished());
        ContactsList.clear();
        if(task.getStatus() == Task.SUCCESS){
            ContactsList.addAll((List<Contacts>)task.getResult());
        }
        adapter.notifyDataSetChanged();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        //通过adapter得到ListView的长度
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight()+listView.getDividerHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }
}
