package com.happytogether.contacts;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.encoder.QRCode;
import com.happytogether.contacts.MainActivity;
import com.yzq.zxinglibrary.encode.CodeCreator;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Monsterkill on 2018/4/16.
 */

public class OtherFragment extends Fragment {
    private int REQUEST_CODE_SCAN = 111;
    public TextView result,editName,editNumber;
    private ImageView qrImage;
    private String contentEtString,strName,strNumber;
    private Button btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_other, container, false);
        result = rootView.findViewById(R.id.resultText);
        editName = rootView.findViewById(R.id.editName);
        editNumber = rootView.findViewById(R.id.editNumber);
        qrImage = rootView.findViewById(R.id.qrImage);
        btn = rootView.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQRCode();
            }
        });
        CardView QRScan = rootView.findViewById(R.id.cardScan);
        QRScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},1);
                } else {
                    //https://github.com/yuzhiqiang1993/zxing
                    Intent intent = new Intent(getActivity(), com.yzq.zxinglibrary.android.CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                }
            }
        });
        return rootView;
    }

    public void getQRCode(){
        strName = editName.getText().toString().trim();
        strNumber = editNumber.getText().toString().trim();

        Bitmap bitmap = null;
        if (Objects.equals(strName, "")||Objects.equals(strNumber,"")) {
            Toast.makeText(getActivity(), "姓名和电话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //转换为vcard格式文本
            contentEtString = "BEGIN:VCARD  \n" +
                    "FN:"+ strName +
                    "\nTEL:"+ strNumber +
                    "\nEND:VCARD";
            bitmap = CodeCreator.createQRCode(contentEtString, 150, 150, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            qrImage.setImageBitmap(bitmap);
        }
    }


}
