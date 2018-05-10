package com.happytogether.contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.happytogether.framework.type.Contacts;

import org.w3c.dom.Text;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<Contacts> {
    private int resourceID;
    private  List<Contacts> list;
    public ContactsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Contacts> objects) {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
        this.list = objects;
    }

    private  class ViewHolder
    {
        private TextView tv_word;
        private TextView tv_name;
        private TextView tv_number;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contacts contacts = getItem(position);
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            holder.tv_name = convertView.findViewById(R.id.contacts_name);
            holder.tv_number = convertView.findViewById(R.id.contacts_number);
            holder.tv_word = convertView.findViewById(R.id.contacts_id);

        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }
        Log.i("chan","changed");
        holder.tv_word.setText(contacts.getHead());
        holder.tv_number.setText(contacts.getNumber());
        holder.tv_name.setText(contacts.getName());
        if(position == 0)
        {
            holder.tv_word.setVisibility(View.VISIBLE);
        }
        else
        {
            String headWord = null;
            headWord = list.get(position-1).getHead();
            if(holder.tv_word.getText().equals(headWord))
            {
                //holder.tv_word.setVisibility(View.GONE);
                holder.tv_word.setText("");
            }
            else
            {
                holder.tv_word.setVisibility(View.VISIBLE);
            }
        }
        convertView.setTag(holder);
        return convertView;
    }
}
