package com.example.androidbroadcastdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private IntentFilter mIntentFilter;
    private  NetworkChangeReceiver mNetworkChangeReceiver;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver,mIntentFilter);

        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.my.broadcast");
                //intent.setComponent(new ComponentName("com.example.androidbroadcastdemo","com.example.androidbroadcastdemo.MyBroadcastReceiver"));
                intent.addFlags(0x01000000);
               // sendBroadcast(intent);
                sendOrderedBroadcast(intent,null);//有序广播，第一个参数为intent，第二个参数为权限相关的字符串
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
           // Toast.makeText(context,"网络状态发生变化",Toast.LENGTH_SHORT).show();
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);//使用getsystemservice方法获取connectivity
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()){//isavailable方法课判断是否有网络
            Toast.makeText(context,"网络已连接",Toast.LENGTH_SHORT).show();
            String typeName ="";
            if (networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                //wifi网咯跳转画面
                typeName = networkInfo.getTypeName();
                Toast.makeText(getApplicationContext(),"网络类型为"+typeName+"网络",Toast.LENGTH_SHORT).show();
            }else if (networkInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                //无线网络跳转的页面
                typeName = networkInfo.getTypeName();
                Toast.makeText(getApplicationContext(),"网络类型为"+typeName+"移动数据网络",Toast.LENGTH_SHORT).show();
            }
            }else {
                Toast.makeText(context,"当前无网络连接",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
