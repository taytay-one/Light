package com.example.Light;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{
    private Switch sw1=null;
    Button toBT=null;
    Intent intent=null;
    public static int nowData=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw1=(Switch) findViewById(R.id.sw1);
        toBT=(Button) findViewById(R.id.toBT);

        sw1.setOnCheckedChangeListener(this);

        toBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(MainActivity.this,BluetoothActivity.class);
                startActivity(intent);
            }
        });           
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (compoundButton.getId() == R.id.sw1) {
            if(compoundButton.isChecked()){
                nowData=11;//按开关按钮后发送数据，发送11即点亮灯
            } else{
                nowData=22;
            }
            Log.i("onCheckedChanged", "nowData: " + nowData);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //软件退出后清空，断开蓝牙操作
        BluetoothActivity.connectThread.cancel();
        BluetoothActivity.connectedThread.cancel();
    }

    public void back(View view) {
        intent=new Intent(MainActivity.this,BluetoothActivity.class);
        startActivity(intent);
    }
}