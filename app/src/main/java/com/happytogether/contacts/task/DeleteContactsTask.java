package com.happytogether.contacts.task;

import com.happytogether.framework.resouce_manager.ResourceManager;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.Contacts;
//删除联系人
public class DeleteContactsTask extends Task {

    private Contacts _contacts;

    public DeleteContactsTask(Contacts contacts){
        _contacts = contacts;
    }

    @Override
    public int exec() {
        if(ResourceManager.getInstance().removeContacts(_contacts)) {
            return Task.SUCCESS;
        }
        return Task.ERROR;
    }
}

