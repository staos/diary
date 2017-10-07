package com.example.diary.activity;

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
import com.example.diary.bean.Feedback;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar toolbar = (Toolbar)findViewById(R.id.feedbackToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        feedback = (EditText)findViewById(R.id.feedbackEdit);
        Button button = (Button)findViewById(R.id.submit);
        button.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit){
            if (!feedback.getText().toString().equals("")){
                Feedback back = new Feedback();
                back.setContent(feedback.getText().toString());
                back.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            Toast.makeText(FeedbackActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                            feedback.setText("");
                        }else{
                            Toast.makeText(FeedbackActivity.this, "反馈失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "请填写好建议", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
