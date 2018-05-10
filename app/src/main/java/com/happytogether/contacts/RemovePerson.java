package com.happytogether.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.zxing.WriterException;
import com.happytogether.contacts.task.RemoveContactsTask;
import com.happytogether.framework.processor.Processor;
import com.happytogether.framework.task.Task;
import com.happytogether.framework.type.Contacts;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Monsterkill on 2018/5/3.
 */
//修改联系人
public class RemovePerson extends AppCompatActivity {
    private EditText remove_number, remove_name;
    private String remove_id;
    private String old_name = "",old_number = "";
    private Button button_remove, button_showqr;
    private ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editperson);
        remove_name = findViewById(R.id.edit_name);
        remove_number = findViewById(R.id.edit_number);
        Bundle bundle = getIntent().getExtras();
        old_name = bundle.getString("name");
        old_number = bundle.getString("number");
        remove_name.setText(old_name);
        remove_number.setText(old_number);
        qrImage = (ImageView) findViewById(R.id.qrimage);
        remove_id = bundle.getString("id");
        button_showqr = (Button) findViewById(R.id.button_showqr);
        //点击显示二维码
        button_showqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQRCode();
            }
        });
        button_remove = findViewById(R.id.button_edit);
        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePerson();
            }
        });

    }
    private void removePerson() {
        String _name = remove_name.getText().toString();
        String _phone = remove_number.getText().toString();
        if (Objects.equals(_name, "") || Objects.equals(_phone, "")) {
            Toast.makeText(RemovePerson.this, "姓名和电话不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Contacts contacts = new Contacts();
            contacts.setName(_name);
            contacts.setNumber(_phone);

            Contacts oldcontacts = new Contacts();
            oldcontacts.setId(remove_id);
            oldcontacts.setName(old_name);
            oldcontacts.setNumber(old_number);

            Task task = new RemoveContactsTask(oldcontacts,contacts);
            Processor.getInstance().process(task);
            while(!task.finished());

            if(task.getStatus() == Task.SUCCESS) {
                Toast.makeText(RemovePerson.this, "联系人" + _name + "已修改", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(RemovePerson.this, "联系人" + _name + "修改失败", Toast.LENGTH_SHORT).show();
            }
            ContactsFragment.updateData();
            finish();
        }
    }

    //获取二维码
    public void getQRCode(){
        String strName = remove_name.getText().toString().trim();
        String strNumber = remove_number.getText().toString().trim();

        Bitmap bitmap = null;
        if (Objects.equals(strName, "")||Objects.equals(strNumber,"")) {
            Toast.makeText(RemovePerson.this, "姓名和电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //转换为vcard格式文本
            String contentEtString = "BEGIN:VCARD  \n" +
                    "FN:"+ strName +
                    "\nTEL:"+ strNumber +
                    "\nEND:VCARD";
            bitmap = CodeCreator.createQRCode(contentEtString, 200, 200, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            qrImage.setImageBitmap(bitmap);
            Toast.makeText(RemovePerson.this,"已生成二维码",Toast.LENGTH_SHORT);
        }
    }

}
