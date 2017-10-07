package com.example.diary.activity;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.diary.R;
import com.example.diary.bean.Mood;
import com.example.diary.bean._User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MoodActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText moodText;
    private static final String TAG = "MoodActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        Toolbar toolbar = (Toolbar)findViewById(R.id.moodToolbar);
        setSupportActionBar(toolbar);
        moodText = (EditText)findViewById(R.id.mood);
        LinearLayout moodLayout = (LinearLayout)findViewById(R.id.moodLayout);
        moodLayout.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.sure:
                _User user = _User.getCurrentUser(_User.class);
                Mood mood = new Mood();
                mood.setAuthor(user);
                mood.setContent(moodText.getText().toString());
                mood.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if ( e == null){
                            Log.d(TAG, "添加数据成功");
                            Log.d(TAG, s);
                        }else{
                            Log.d(TAG, "添加数据失败" + e.getMessage());
                        }
                    }
                });
                moodText.setText("");
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.moodLayout){
            moodText.requestFocus();
            showKeyboard();
        }
    }
    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        moodText.requestFocus();
        imm.showSoftInput(moodText, 0);
    }
}
