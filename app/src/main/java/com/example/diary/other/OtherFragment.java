package com.example.diary.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.diary.R;
import com.example.diary.adapter.OtherAdapter;
import com.example.diary.bean.Other;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 604 on 2017/9/22.
 */

public class OtherFragment extends Fragment {
    private View view;
    private List<Other> otherList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.other_fragment,container,false);
        }
        if (otherList.size() == 0){
            initData();
        }
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        OtherAdapter otherAdapter = new OtherAdapter(otherList,getActivity());
        recyclerView.setAdapter(otherAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        return view;
    }
    private void initData(){
        Other otherOne = new Other("任务倒计时",R.drawable.test);
        Other otherTwo = new Other("时间管理",R.drawable.time_manager);
        otherList.add(otherOne);
        otherList.add(otherTwo);
    }
}
