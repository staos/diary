package com.example.diary.other;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.diary.R;
import com.example.diary.activity.WriteDiary;
import com.example.diary.adapter.DiaryAdapter;
import com.example.diary.bean.CollectionMood;
import com.example.diary.bean._User;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周要明 on 2017/8/31.
 */

public class SorryFragment extends Fragment {
    private View view;
    private List<CollectionMood> moodList = new ArrayList<>();
    private static final String TAG = "SorryFragment";
    private RecyclerView recycler;
    private DiaryAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.sorry_fragment,container,false);
        }
        initializationData();
        EventBus.getDefault().register(this);
        Toolbar toolbar = (Toolbar)view.findViewById(R.id.collectionToolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        recycler = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
        recycler.setLayoutManager(layoutManager);
        adapter = new DiaryAdapter(moodList,getActivity());
        recycler.setAdapter(adapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.add,menu);
    }

    private void initializationData(){
        List<CollectionMood> moods = DataSupport.findAll(CollectionMood.class);
        if (moodList != null)
            moodList.clear();
        for(CollectionMood mood : moods)
            moodList.add(mood);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add){
            Intent intent = new Intent(getActivity(), WriteDiary.class);
            startActivityForResult(intent,1);
        }
        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void onMessageEvent(ChangeEvent event){
        String msg = "onMessageEvent收到了消息：" + event.getmMsg();
        Log.d(TAG, "onMessageEvent: " + msg);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1:
                initializationData();
                adapter.notifyDataSetChanged();
                break;
        }
    }
}
