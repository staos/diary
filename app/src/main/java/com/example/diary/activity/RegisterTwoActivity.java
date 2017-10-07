package com.example.diary.activity;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diary.R;
import com.example.diary.bean._User;
import com.example.diary.other.MyApplication;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterTwoActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText textOne;
    private EditText textTwo;
    private String passwordOne;
    private String passwordTwo;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textOne = (EditText)findViewById(R.id.passwordOne);
        textTwo = (EditText)findViewById(R.id.passwordTwo);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Button button = (Button)findViewById(R.id.sure_register);
        button.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent intent = new Intent(MyApplication.getContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        passwordOne = textOne.getText().toString();
        passwordTwo = textTwo.getText().toString();
        switch(v.getId()){
            case R.id.sure_register:
                if(passwordOne.equals(passwordTwo)){
                    BmobUser user = new BmobUser();
                    user.setUsername(phoneNumber);
                    user.setPassword(passwordTwo);
                    user.signUp(new SaveListener<_User>() {
                        @Override
                        public void done(_User user, BmobException e) {
                            if (e==null){
                                Toast.makeText(RegisterTwoActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegisterTwoActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent intent = new Intent(this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
        }
    }
}
