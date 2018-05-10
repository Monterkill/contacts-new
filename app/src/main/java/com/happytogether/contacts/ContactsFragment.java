package com.happytogether.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.happytogether.contacts.task.QueryAllContactsTask;
import com.happytogether.framework.processor.Processor;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.Contacts;
import com.happytogether.contacts.task.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Monsterkill on 2018/4/16.
 */

public class ContactsFragment extends Fragment{
    public static ContactsAdapter adapter;
    private static List<Contacts> contactsList = new ArrayList<>();
    public final static int REQUEST_REMOVE=100;
    public final static int REQUEST_DELETE=100;

    public Context context;

    /*/public Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                updateData();
            }
        }
    };/*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab_contacts, container, false);
        ListView contactsView = (ListView) rootView.findViewById(R.id.contacts_view);
        adapter = new ContactsAdapter(getActivity(), R.layout.item_contacts,contactsList);
        getContactsList();
        setListViewHeightBasedOnChildren(contactsView);
        contactsView.setAdapter(adapter);
        contactsView.setOnItemClickListener(new MyListener() );
        contactsView.setOnCreateContextMenuListener(new MyListener());

        //   Task task = new AutoFreshContactsFragmentTask(this, 2000);
        //    Processor.getInstance().process(task);

        return rootView;
    }

    public static void updateData(){
        Task task = new QueryAllContactsTask();
        Processor.getInstance().process(task);
        while(!task.finished());
        contactsList.clear();
        if(task.getStatus() == Task.SUCCESS){
            contactsList.addAll((List<Contacts>)task.getResult());
        }
        adapter.notifyDataSetChanged();
    }

    //获取联系人
    public void getContactsList() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},1);
        }
//        Cursor cs = getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null, null);
//        if (cs != null && cs.getCount() > 0) {
//            for (cs.moveToFirst(); (!cs.isAfterLast()); cs.moveToNext()) {
//                String contastsName = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));  //姓名
//                String contastsNumber = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));  //号码
//                String contastsID = cs.getString(cs.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));//ID
//                // 去掉手机号中的空格和“-”
//                contastsNumber = contastsNumber.replace(" ", "").replace("-", "").replace("+86", "");
//                contactsList.add(new Contacts(contastsName, contastsNumber));
//                //Log.v("tag", "strPhoneNumber:" + contastsNumber);
//            }
//            adapter.notifyDataSetChanged();
//            cs.close();
//            Log.d("","游标closd");
//        }
//        else {
//            cs.close();
//        }
        updateData();
    }

    class MyListener implements AdapterView.OnItemClickListener, View.OnCreateContextMenuListener{
        //点击拨打电话
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String phoneNumber = (String) ((TextView)view.findViewById(R.id.contacts_number)).getText();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        //长按弹出菜单
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("选择操作");
            menu.add(0,0,0,"编辑");
            menu.add(0,1,0,"删除");
        }
    }
    //菜单点击事件
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterContextMenuInfo it =  (AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("name",adapter.getItem(it.position).getName());
                bundle.putString("number",adapter.getItem(it.position).getNumber());
                bundle.putString("id",adapter.getItem(it.position).getId());
                Intent intent = new Intent(context, RemovePerson.class).putExtras(bundle);
                startActivityForResult(intent, REQUEST_REMOVE);
                Toast.makeText(getContext(), "编辑联系人的方法", Toast.LENGTH_SHORT).show();
                return true;
            case 1:
                DeletePerson(it.position);
                updateData();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void DeletePerson(int pos)
    {
        Contacts contacts = adapter.getItem(pos);
        Task task = new DeleteContactsTask(contacts);
        Processor.getInstance().process(task);
        while (!task.finished()) ;

        if (task.getStatus() == Task.SUCCESS) {
            Toast.makeText(getContext(),"联系人" + contacts.getName() + "已删除", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "联系人" + contacts.getName() + "删除失败", Toast.LENGTH_SHORT).show();
        }
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        //通过adapter得到ListView的长度
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight()+listView.getDividerHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }


}
