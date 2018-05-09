package com.happytogether.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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


import com.happytogether.contacts.task.AddContactsTask;
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

public class AddPerson extends AppCompatActivity {
    private EditText add_number, add_name;
    private Button button_add;
    private CardView QRScan;
    private int REQUEST_CODE_SCAN = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addperson);
        add_name = findViewById(R.id.add_name);
        add_number = findViewById(R.id.add_number);
        button_add = findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPerson();
            }
        });

        QRScan = findViewById(R.id.cardScan);
        QRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(AddPerson.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddPerson.this,new String[]{Manifest.permission.CAMERA},1);
                } else {
                    //https://github.com/yuzhiqiang1993/zxing
                    Intent intent = new Intent(AddPerson.this, com.yzq.zxinglibrary.android.CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(com.yzq.zxinglibrary.common.Constant.CODED_CONTENT);

                // 按指定模式在字符串查找
                String pattern = " (\\s)(FN:)(.*)(\\s)(TEL:)(.*)(\\s)";

                // 创建 Pattern 对象
                Pattern r = Pattern.compile(pattern);

                // 现在创建 matcher 对象
                Matcher m = r.matcher(content);
                if (m.find( )) {
                    add_name.setText( m.group(3));
                    add_number.setText( m.group(6));
                    Log.d("Found Name " , m.group(3) );
                    Log.d("Found Number " , m.group(6) );
                    Toast.makeText(this,"获得联系人扫描结果",Toast.LENGTH_SHORT);
                } else {
                    Log.d("ERR","NO MATCH");
                }
            }
        }
    }


    private void addPerson() {
        String _name = add_name.getText().toString();
        String _phone = add_number.getText().toString();
        if (Objects.equals(_name, "") || Objects.equals(_phone, "")) {
            Toast.makeText(AddPerson.this, "姓名和电话不能为空", Toast.LENGTH_SHORT).show();
        } else {
            Contacts contacts = new Contacts();
            contacts.setName(_name);
            contacts.setNumber(_phone);
            Task task = new AddContactsTask(contacts);
            Processor.getInstance().process(task);
            while(!task.finished());

            if(task.getStatus() == Task.SUCCESS) {
                Toast.makeText(AddPerson.this, "联系人" + _name + "已添加", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(AddPerson.this, "联系人" + _name + "添加失败", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}
