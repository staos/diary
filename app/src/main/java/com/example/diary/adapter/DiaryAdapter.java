package com.example.diary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.diary.R;
import com.example.diary.activity.DisplayActivity;
import com.example.diary.bean.CollectionMood;
import com.example.diary.other.MyApplication;

import java.util.List;

/**
 * Created by 周要明 on 2017/9/4.
 */

public class DiaryAdapter extends RecyclerView.Adapter <DiaryAdapter.ViewHolder>{
    private static final String TAG = "DiaryAdapter";
    private List<CollectionMood> mCollectionMood;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView timeText;
        TextView diaryContent;
        ImageView imageView;

        public ViewHolder(View view){
            super(view);
            itemView = view;
            timeText = (TextView)view.findViewById(R.id.time);
            diaryContent = (TextView)view.findViewById(R.id.diaryText);
            imageView = (ImageView)view.findViewById(R.id.diaryPhoto);
        }
    }
    public DiaryAdapter(List<CollectionMood> moodList,Context context){
        mCollectionMood = moodList;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                CollectionMood mood = mCollectionMood.get(position);
                Intent intent = new Intent(MyApplication.getContext(),DisplayActivity.class);
                intent.putExtra("diary",mood);
                mContext.startActivity(intent);
                Log.d(TAG, "onClick: 点击");
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CollectionMood mood = mCollectionMood.get(position);
        holder.timeText.setText(mood.getTime());
        holder.diaryContent.setText(mood.getContent());
        Glide.with(MyApplication.getContext())
                .load(mood.getImageHeadUrl())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mCollectionMood.size();
    }
}
