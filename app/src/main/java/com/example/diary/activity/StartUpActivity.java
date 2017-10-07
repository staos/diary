package com.example.diary.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.diary.R;
import com.example.diary.bean._User;
import com.example.diary.other.MyApplication;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                BmobUser bmobUser = BmobUser.getCurrentUser(_User.class);
                if (bmobUser != null){
                    Intent intent = new Intent(MyApplication.getContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(MyApplication.getContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }).start();
    }
}
