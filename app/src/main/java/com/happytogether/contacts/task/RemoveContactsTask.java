package com.happytogether.contacts.task;

import com.happytogether.framework.resouce_manager.ResourceManager;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.Contacts;
//修改联系人
public class RemoveContactsTask extends Task {

    private Contacts _contacts;
    private Contacts _oldcontacts;

    public RemoveContactsTask(Contacts oldcontacts,Contacts contacts){
        _contacts = contacts;
        _oldcontacts = oldcontacts;
    }

    @Override
    public int exec() {
        if(ResourceManager.getInstance().modifyContacts(_oldcontacts,_contacts)) {
            return Task.SUCCESS;
        }
        return Task.ERROR;
    }
}

