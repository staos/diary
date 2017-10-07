package com.example.diary.activity;


import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.diary.R;
import com.idtk.smallchart.chart.PieChart;
import com.idtk.smallchart.data.PieData;
import com.idtk.smallchart.interfaces.iData.IPieData;

import java.util.ArrayList;


public class TimeManagerActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<IPieData> mPieDataList = new ArrayList<>();
    private PieChart mPieChart;
    public static final int UPDATE_UI = 1;
    private EditText sport;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case UPDATE_UI:
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_manager);
        initData();
        mPieChart = (PieChart)findViewById(R.id.pie_chart);
        mPieChart.setAxisColor(Color.WHITE);
        mPieChart.isTouch = false;
        mPieChart.setDataList(mPieDataList);
        mPieChart.setAxisTextSize(40);
        View viewOne = (View)findViewById(R.id.ColorOne);
        viewOne.setBackgroundColor(Color.RED);
        View viewTwo = (View)findViewById(R.id.ColorTwo);
        viewTwo.setBackgroundColor(Color.GREEN);
        View viewThree =(View)findViewById(R.id.ColorThree);
        viewThree.setBackgroundColor(Color.BLUE);
        View viewFour = (View)findViewById(R.id.ColorFour);
        viewFour.setBackgroundColor(Color.MAGENTA);
        View viewFive = (View)findViewById(R.id.ColorFive);
        viewFive.setBackgroundColor(Color.CYAN);
    }
    private void initData(){
        PieData pieDataOne = new PieData();
        pieDataOne.setName("专业学习");
        pieDataOne.setValue(10);
        pieDataOne.setColor(Color.RED);
        mPieDataList.add(pieDataOne);
        PieData pieDataTwo = new PieData();
        pieDataTwo.setName("实验室学习");
        pieDataTwo.setValue(50);
        pieDataTwo.setColor(Color.GREEN);
        mPieDataList.add(pieDataTwo);
        PieData pieDataThree = new PieData();
        pieDataThree.setName("运动");
        pieDataThree.setValue(15);
        pieDataThree.setColor(Color.BLUE);
        mPieDataList.add(pieDataThree);
        PieData pieDataFive = new PieData();
        pieDataFive.setName("游戏");
        pieDataFive.setValue(0);
        pieDataFive.setColor(Color.MAGENTA);
        mPieDataList.add(pieDataFive);
        PieData pieDataFour = new PieData();
        pieDataFour.setName("其他");
        pieDataFour.setValue(10);
        pieDataFour.setColor(Color.CYAN);
        mPieDataList.add(pieDataFour);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sure){
            Message message = new Message();
            message.what = UPDATE_UI;
            handler.sendMessage(message);
        }
    }
}
