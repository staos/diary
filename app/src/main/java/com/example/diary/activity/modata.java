package com.example.diary.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.diary.R;
import com.example.diary.bean._User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class modata extends AppCompatActivity {
    private EditText editAutograph;
    private EditText editName;
    private static final String TAG = "modata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modata);
        Toolbar toolbar = (Toolbar)findViewById(R.id.dataToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        _User user = _User.getCurrentUser(_User.class);
        editName = (EditText)findViewById(R.id.nickName);
        editAutograph = (EditText)findViewById(R.id.modificationAutograph);
        editName.setText(user.getNickName());
        editAutograph.setText(user.getAutograph());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool,menu);
        return true;
    }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sure:
                _User user = _User.getCurrentUser(_User.class);
                user.setAutograph(editAutograph.getText().toString());
                user.setNickName(editName.getText().toString());
                user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Log.d(TAG, "更新用户信息成功");
                        }else{
                            Log.d(TAG, "更新用户信息失败");
                        }
                    }
                });
                finish();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
