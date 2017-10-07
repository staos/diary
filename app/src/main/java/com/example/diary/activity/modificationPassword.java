package com.example.diary.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diary.R;
import com.example.diary.bean._User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class modificationPassword extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private EditText editOne;
    private EditText editTwo;
    private EditText editThree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_password);
        Toolbar toolbar = (Toolbar)findViewById(R.id.passwordToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        editOne = (EditText)findViewById(R.id.passOne);
        editTwo = (EditText)findViewById(R.id.passTwo);
        editThree = (EditText)findViewById(R.id.newPassword);
        button = (Button)findViewById(R.id.surePassword);
        button.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.surePassword){
            _User user = _User.getCurrentUser(_User.class);
            if(editTwo.getText().toString().equals(editThree.getText().toString())){
                _User.updateCurrentUserPassword(editOne.getText().toString(), editThree.getText().toString(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toast.makeText(modificationPassword.this, "更改密码成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(modificationPassword.this, "请输入正确密码", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
