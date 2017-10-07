package com.example.diary.other;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.diary.R;
import com.example.diary.activity.MoodActivity;
import com.example.diary.adapter.FragmentAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by 周要明 on 2017/8/4.
 */

public class  PokeballFragment extends Fragment implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private WholeFragment wholeFragment;
    private HotFragment hotFragment;
    private MineFragment mineFragment;
    private ViewPager viewPager;
    private TextView textOne;
    private TextView textTwo;
    private TextView textThree;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pokeball_fragment,container,false);
        wholeFragment = new WholeFragment();
        hotFragment = new HotFragment();
        mineFragment = new MineFragment();
        textOne = (TextView)view.findViewById(R.id.wholes);
        textTwo = (TextView)view.findViewById(R.id.hots);
        textThree = (TextView)view.findViewById(R.id.mines);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(wholeFragment);
        fragments.add(hotFragment);
        fragments.add(mineFragment);
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(),fragments);
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        FrameLayout whole = (FrameLayout)view.findViewById(R.id.whole);
        FrameLayout hot = (FrameLayout) view.findViewById(R.id.hot);
        FrameLayout mine = (FrameLayout) view.findViewById(R.id.mine);;
        whole.setOnClickListener(this);
        hot.setOnClickListener(this);
        mine.setOnClickListener(this);
        FloatingActionButton floatingActionButton = (FloatingActionButton)view.findViewById(R.id.write);
        floatingActionButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.whole:;
                textOne.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
                textTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                textThree.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                viewPager.setCurrentItem(0);
                break;
            case R.id.hot:
                textOne.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                textTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
                textThree.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                viewPager.setCurrentItem(1);
                break;
            case R.id.mine:
                textOne.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                textTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                textThree.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
                viewPager.setCurrentItem(2);
                break;
            case R.id.write:
                Intent intent = new Intent(getActivity(),MoodActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0){
            textOne.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
            textTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textThree.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        }else if (position == 1){
            textOne.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
            textThree.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        }else if(position == 2){
            textOne.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textTwo.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textThree.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
