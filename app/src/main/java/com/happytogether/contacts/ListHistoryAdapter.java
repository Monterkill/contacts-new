package com.happytogether.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.happytogether.contacts.util.MyUtil;
import com.happytogether.framework.type.CallRecord;

import java.util.List;

public class ListHistoryAdapter extends ArrayAdapter<CallRecord> {
    private String changeStr = "";
    //
    private List<CallRecord> mlistdatas;
    private LayoutInflater mInflater;

    public ListHistoryAdapter(Context mcontext, int textViewResourceId, List<CallRecord> mlistdatas) {
        super(mcontext, textViewResourceId, mlistdatas);
        this.mlistdatas = mlistdatas;
        mInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return mlistdatas.size();
    }

//    @Override
//    public Object getItem(int i) {
//        return mlistdatas.get(i);
//    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private int resourceID;
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CallRecord record = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
        TextView name = view.findViewById(R.id.history_name);
        TextView number= view.findViewById(R.id.history_number);
        TextView type = view.findViewById(R.id.history_type);
        TextView date = view.findViewById(R.id.history_date);
        TextView duration = view.findViewById(R.id.history_duration);
        TextView location= view.findViewById(R.id.history_location);

        name.setText(record.getName());
        number.setText(record.getNumber());
        type.setText(MyUtil.getCallStatusStr(record.getStatus()));
        date.setText(MyUtil.formatCallDate(record.getStartTime()));
        duration.setText(MyUtil.formatTs(record.getDuration()));
        location.setText((String)record.getFeature("location"));
        return view;
    }

    //这个方法很重要，editText监听文本变化需要用到
    public void changeText(String textStr) {
        this.changeStr = textStr;
        //别忘了，notifyDataSetChanged（）一定要调用，一定要调用，一定要调用，重要事说三遍
        notifyDataSetChanged();

    }
}
