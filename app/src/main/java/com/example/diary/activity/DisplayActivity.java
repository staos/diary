package com.example.diary.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diary.R;
import com.example.diary.bean.CollectionMood;

public class DisplayActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imageOne;
    private ImageView imageTwo;
    private TextView textView;
    private CollectionMood mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        imageOne = (ImageView)findViewById(R.id.imageDiaryOne);
        imageOne.setOnClickListener(this);
        imageTwo = (ImageView)findViewById(R.id.imageDiaryTwo);
        imageTwo.setOnClickListener(this);
        textView = (TextView)findViewById(R.id.diaryTextView);
        initializationData();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.imageDiaryOne:
                pictureBrowser(mood.getImageHeadUrl());
                break;
            case R.id.imageDiaryTwo:
                pictureBrowser(mood.getImageBackgroundUrl());
                break;
        }
    }
    private void initializationData(){
        mood = (CollectionMood)getIntent().getSerializableExtra("diary");
        Glide.with(DisplayActivity.this)
                .load(mood.getImageBackgroundUrl())
                .into(imageOne);
        Glide.with(DisplayActivity.this)
                .load(mood.getImageHeadUrl())
                .into(imageTwo);
        textView.setText(mood.getContent());
    }
    private void pictureBrowser(String uri){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(uri),"image/*");
        startActivity(intent);
    }
}
