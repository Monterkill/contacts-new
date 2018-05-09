package com.happytogether.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.happytogether.contacts.util.MyUtil;
import com.happytogether.framework.type.CallRecord;

import java.util.List;

/**
 * Created by Monsterkill on 2018/4/20.
 */

public class HistoryAdapter extends ArrayAdapter<CallRecord> {

    private int resourceID;
    public HistoryAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<CallRecord> objects) {
        super(context,textViewResourceId, objects);
        resourceID = textViewResourceId;
    }

    @NonNull
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
}