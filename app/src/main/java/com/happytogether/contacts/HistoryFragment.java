package com.happytogether.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.happytogether.contacts.task.QueryAllCallRecordTask;
import com.happytogether.contacts.task.QueryCallRecordByKeyWordsTask;
import com.happytogether.framework.log.LogBus;
import com.happytogether.framework.processor.Processor;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.CallRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * Created by Monsterkill on 2018/4/16.
 */

public class HistoryFragment extends Fragment {
    private HistoryAdapter adapter;
    private List<CallRecord> HistoryList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_history, container, false);
        ListView historyView = rootView.findViewById(R.id.history_view);
        adapter = new HistoryAdapter(getActivity(), R.layout.item_history, HistoryList);
        getCallHistoryList();
        setListViewHeightBasedOnChildren(historyView);
        historyView.setAdapter(adapter);
        historyView.setOnItemClickListener(new MyListener());
        return rootView;
    }

    public void getCallHistoryList() {
//        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CALL_LOG)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG},1);
//        }
        updateData();
//        filterData();
    }

    public void updateData(){
        Task task = new QueryAllCallRecordTask();
        Processor.getInstance().process(task);
        while(!task.finished());
        HistoryList.clear();
        if(task.getStatus() == Task.SUCCESS){
            HistoryList.addAll((List<CallRecord>)task.getResult());
        }
        adapter.notifyDataSetChanged();
    }

    public void filterData(String temp){
        Task task = new QueryCallRecordByKeyWordsTask(temp);
        Processor.getInstance().process(task);
        while(!task.finished());
        HistoryList.clear();
        if(task.getStatus() == Task.SUCCESS){
            HistoryList.addAll((List<CallRecord>)task.getResult());
        }
        /*Task test_task = new QueryCallRecordByKeyWordsTask("hjz");
        Processor.getInstance().process(test_task);
        while(!test_task.finished()){ }//加载完成后再进行下一步
        if(test_task.getStatus() == Task.SUCCESS){
            String s = "";
            for(CallRecord r : (List<CallRecord>)test_task.getResult()){
                s += (r.getStartTime() + "@" + r.getName() + "\n");
            }
            LogBus.Log(LogBus.DEBUGTAGS, "finish task" + s);
        }*/
    }

    class MyListener implements AdapterView.OnItemClickListener, View.OnCreateContextMenuListener{
        //点击拨打电话
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String phoneNumber = (String) ((TextView)view.findViewById(R.id.contacts_number)).getText();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        //长按弹出菜单
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("选择操作");
            menu.add(0,0,0,"删除");
        }
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
