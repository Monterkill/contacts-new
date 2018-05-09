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
        /*List<String> mlistdata = new ArrayList<>();
        mlistdata.add("我的第一写作，简书");
        mlistdata.add("很多年以后，开始相信，所谓爱情，都是因为寂寞");
        mlistdata.add("曾经企盼有地老天荒的爱情，有海枯石烂的情缘");
        mlistdata.add("爱情不是游戏，因为我们玩不起");
        mlistdata.add("爱情不是游戏，因为我们玩不起");
        mlistdata.add("爱，不是一个人的独角戏，而是两个人的对手戏");
        mlistdata.add("爱，不是一个人的独角戏，而是两个人的对手戏");
        mlistdata.add("每个人心里，都有个过不去的人");
        mlistdata.add("每个人心里，都有个过不去的人");
        mlistdata.add("异地恋，恋的不仅仅是爱情，还有一种坚持");
        mlistdata.add("异地恋，恋的不仅仅是爱情，还有一种坚持");
        mlistdata.add("曾经，一遍遍的思念，一遍遍的在心里抱怨");
        mlistdata.add("爱与被爱同样是受伤害，谁先不爱谁先离开");
        mlistdata.add("还有de小星");
        return mlistdata;*/

    }
}
