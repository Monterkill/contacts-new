package com.happytogether.contacts;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.happytogether.framework.type.CallRecord;

import java.util.List;

public class ListHistoryAdapter extends BaseAdapter {
    private String changeStr = "";
    //
    private List<CallRecord> mlistdatas;
    private LayoutInflater mInflater;

    public ListHistoryAdapter(Context mcontext, List<CallRecord> mlistdatas) {
        this.mlistdatas = mlistdatas;
        mInflater = LayoutInflater.from(mcontext);
    }

    @Override
    public int getCount() {
        return mlistdatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mlistdatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = mInflater.inflate(R.layout.search_layout_item, null);
        TextView tv_name = view.findViewById(R.id.text_name);
        tv_name.setText(mlistdatas.get(i).getName());
        //处理关键字颜色变化
        if (null != mlistdatas.get(i) && mlistdatas.get(i).getName().contains(changeStr)) {
            int index = mlistdatas.get(i).getName().indexOf(changeStr);
            int len = changeStr.length();
            Spanned temp = Html.fromHtml(mlistdatas.get(i).getName().substring(0, index)
                    + "<font color=#ff0000>"
                    + mlistdatas.get(i).getName().substring(index, index + len) + "</font>"
                    + mlistdatas.get(i).getName().substring(index + len, mlistdatas.get(i).getName().length()));
            tv_name.setText(temp);
        } else {
            tv_name.setText(mlistdatas.get(i).getName());
        }

        return view;
    }

    //这个方法很重要，editText监听文本变化需要用到
    public void changeText(String textStr) {
        this.changeStr = textStr;
        //别忘了，notifyDataSetChanged（）一定要调用，一定要调用，一定要调用，重要事说三遍
        notifyDataSetChanged();

    }
}
