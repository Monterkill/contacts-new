package com.happytogether.framework.type;

import android.support.annotation.NonNull;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.Objects;

public class Contacts extends FeatureObj implements  Comparable<Contacts>{

    private String _number;
    private String _name;
    private String _id;
    private String _head = "28";
    public Contacts(){
        _name = "";
        _number = "";
    }

    public void setNumber(String number){
        _number = number;
    }

    public String getNumber(){
        return _number;
    }

    public void setName(String name){
        _name = name;
    }

    public String getName(){
        return _name;
    }

    public void setId(String id) { _id = id;}

    public String getId() { return _id; }

    public String getHead()
    {
        if(_head == "28") {
            try {
                _head = PinyinHelper.getShortPinyin(_name.substring(0, 1)).toUpperCase();
            } catch (PinyinException e) {
                e.printStackTrace();
            }
        }
        return _head;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }

        Contacts contacts = (Contacts) o;
        return Objects.equals(_number, contacts._number)
                && Objects.equals(_name, contacts._name);
    }

    @Override
    public int compareTo(@NonNull Contacts contacts) {
        return this.getHead().compareTo(contacts.getHead());
    }
}
