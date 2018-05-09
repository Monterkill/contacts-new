package com.happytogether.contacts.task;

import com.happytogether.contacts.ContactsFragment;
import com.happytogether.framework.task.Task;

public class AutoFreshContactsFragmentTask extends Task {

    private ContactsFragment _fragment;
    private int _inteval;

    public AutoFreshContactsFragmentTask(ContactsFragment fragment, int interval){
        _fragment = fragment;
        _inteval = interval;
        setPriority(Task.BACK);
    }

    @Override
    public int exec() {
        while(true){
            try {
                Thread.sleep(_inteval);
            }catch (Exception e){
                e.printStackTrace();
            }
            _fragment.msgHandler.sendEmptyMessage(100);
        }
    }
}
