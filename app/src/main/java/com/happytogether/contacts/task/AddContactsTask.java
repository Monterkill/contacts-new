package com.happytogether.contacts.task;

import com.happytogether.framework.resouce_manager.ResourceManager;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.Contacts;

public class AddContactsTask extends Task {

    private Contacts _contacts;

    public AddContactsTask(Contacts contacts){
        _contacts = contacts;
    }

    @Override
    public int exec() {
        if(ResourceManager.getInstance().addContacts(_contacts)) {
            return Task.SUCCESS;
        }
        return Task.ERROR;
    }
}

