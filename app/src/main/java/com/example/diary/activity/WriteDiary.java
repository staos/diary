package com.example.diary.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.diary.R;
import com.example.diary.bean.CollectionMood;
import com.example.diary.bean.Mood;
import com.example.diary.bean._User;
import com.example.diary.other.ChangeEvent;
import com.example.diary.other.ManFragment;
import com.example.diary.other.MyApplication;
import com.squareup.picasso.Picasso;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import org.greenrobot.eventbus.EventBus;
import org.litepal.tablemanager.Connector;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

public class WriteDiary extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "WriteDiary";
    private TextView time;
    private TextView address;
    private TextView weather;
    private EditText diaryContent;
    private ImageButton button;
    private ImageView imageOne;
    private ImageView imageTwo;
    private static final int REQUEST_CODE_CHOOSE = 4;
    private List<Uri> mSelected;
    private String imageOneUri;
    private String imageTwoUri;
    private boolean calendar;
    public LocationClient mLocationClient;
    _User user= _User.getCurrentUser(_User.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        setContentView(R.layout.activity_write_diary);
        BmobQuery<_User> query = new BmobQuery<>();
        query.getObject(user.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null){
                    calendar = user.getSwitchOne();
                    Log.d(TAG, "done: 查询数据成功");
                }
            }
        });
        time = (TextView)findViewById(R.id.timeText);
        address = (TextView)findViewById(R.id.address);
        weather = (TextView)findViewById(R.id.weather);
        diaryContent = (EditText)findViewById(R.id.writeText);
        button = (ImageButton)findViewById(R.id.selectPhoto);
        button.setOnClickListener(this);
        imageOne = (ImageView)findViewById(R.id.addPhotoOne);
        imageTwo = (ImageView)findViewById(R.id.addPhotoTwo);
        Toolbar toolbar = (Toolbar)findViewById(R.id.writeToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DATE));
        time.setText(year + "/" + month + "/" + day);
        //获取位置信息
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
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
                if (!diaryContent.getText().toString().equals("")){
                    CollectionMood mood = new CollectionMood();
                    mood.setContent(diaryContent.getText().toString());
                    mood.setImageHeadUrl(imageOneUri);
                    mood.setImageBackgroundUrl(imageTwoUri);
                    mood.setTime(time.getText().toString());
                    mood.save();
                    Log.d(TAG, "日记存入成功");
                    if (calendar == true){
                        Mood mood1 = new Mood();
                        mood1.setAuthor(user);
                        mood1.setContent(diaryContent.getText().toString());
                        mood1.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null){
                                    Log.d(TAG, "done: 日记同步成功");
                                }else{
                                    Log.d(TAG, "done: 日记同步失败 " + e.getMessage());
                                }
                            }
                        });
                    }
                    EventBus.getDefault().post(new ChangeEvent("日记完成"));
                    finish();
                }else{
                    Toast.makeText(this, "亲，你还没写下你的时光", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.selectPhoto){
            chooseFromGallery();
        }
    }
    private void chooseFromGallery(){
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG,MimeType.PNG,MimeType.GIF))
                .countable(false)
                .maxSelectable(2)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .theme(R.style.Matisse_Zhihu)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CHOOSE){
            if (data != null){
                mSelected = Matisse.obtainResult(data);
                for (int i = 0; i < mSelected.size(); i++){
                    if (i == 0){
                        imageOneUri = String.valueOf(mSelected.get(i));
                        Glide.with(MyApplication.getContext())
                                .load(mSelected.get(i))
                                .into(imageOne);
                    }else{
                        imageTwoUri = String.valueOf(mSelected.get(i));
                        Glide.with(MyApplication.getContext())
                                .load(mSelected.get(i))
                                .into(imageTwo);
                    }
                }
            }
        }
    }
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            address.setText(bdLocation.getProvince() + bdLocation.getCity());
        }
    }
}
