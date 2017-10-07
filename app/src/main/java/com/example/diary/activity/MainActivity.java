package com.example.diary.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.diary.R;
import com.example.diary.bean._User;
import com.example.diary.other.DiaryFragment;
import com.example.diary.other.ManFragment;
import com.example.diary.other.OtherFragment;
import com.example.diary.other.PokeballFragment;
import com.example.diary.other.SorryFragment;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;


public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    private BottomNavigationBar bottomNavigationBar;
    private DiaryFragment diaryFragment;
    private ManFragment manFragment;
    private PokeballFragment pokeballFragment;
    private SorryFragment sorryFragment;
    private OtherFragment otherFragment;
    private boolean calendar;
    private static final String TAG = "MainActivity";
    private int mBackKeyPressedTimes = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        diaryFragment = new DiaryFragment();
        manFragment = new ManFragment();
        sorryFragment = new SorryFragment();
        pokeballFragment = new PokeballFragment();
        diaryFragment = new DiaryFragment();
        otherFragment = new OtherFragment();
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .setActiveColor(R.color.lightBlue)
                .setInActiveColor(R.color.gray)
                .setBarBackgroundColor(R.color.white);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.pokeball,R.string.tab_pokeball))
                .addItem(new BottomNavigationItem(R.drawable.note,R.string.tab_diary))
                .addItem(new BottomNavigationItem(R.drawable.man,R.string.tab_man))
                .addItem(new BottomNavigationItem(R.drawable.other,"其他"))
                .setFirstSelectedPosition(1)
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_content,sorryFragment);
        transaction.commit();
        _User user = _User.getCurrentUser(_User.class);
        BmobQuery<_User> query = new BmobQuery<>();
        query.getObject(user.getObjectId(), new QueryListener<_User>() {
            @Override
            public void done(_User user, BmobException e) {
                if (e == null){
                    calendar = user.getSwitchTwo();
                    Log.d(TAG, "done: 查询成功");
                }else{
                    Log.d(TAG, "done: 查询失败 " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onTabSelected(int position) {//当第position个tab被选中时，调用此方法，这里可以完成对fragment的切换
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch(position){
            case 0:
                transaction.replace(R.id.ll_content,pokeballFragment);
                break;
            case 1:
                transaction.replace(R.id.ll_content,sorryFragment);
                break;
            case 2:
                transaction.replace(R.id.ll_content,manFragment);
                break;
            case 3:
                transaction.replace(R.id.ll_content,otherFragment);
                break;
        }
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {//对未被选中的tab进行处理，其中position仍然是被选中的tab

    }

    @Override
    public void onTabReselected(int position) {//当被选中的tab再一次被点击时调用此方法
    }

    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0){
            Toast.makeText(this, "再按一次退出程序 ", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        }else{
            finish();
        }
        super.onBackPressed();
    }
}
