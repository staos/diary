package com.example.diary.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diary.R;
import com.example.diary.activity.TimeManagerActivity;
import com.example.diary.bean.Other;
import com.example.diary.other.MyApplication;

import java.util.List;

/**
 * Created by 604 on 2017/9/24.
 */

public class OtherAdapter extends RecyclerView.Adapter<OtherAdapter.ViewHolder>{
    private List<Other> mOtherList;
    private Context mContext;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View otherView;
        ImageView imageView;
        TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            otherView = itemView;
            imageView = (ImageView)itemView.findViewById(R.id.image_view);
            textView = (TextView)itemView.findViewById(R.id.text_view);
        }
    }
    public OtherAdapter(List<Other> otherList, Context context){
        mOtherList = otherList;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.otherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Other other = mOtherList.get(position);
                if (other.getImageId() == R.drawable.test){

                }else if (other.getImageId() == R.drawable.time_manager){
                    Intent intent = new Intent(MyApplication.getContext(), TimeManagerActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Other other = mOtherList.get(position);
        holder.imageView.setImageResource(other.getImageId());
        holder.textView.setText(other.getName());
    }


    @Override
    public int getItemCount() {
        return mOtherList.size();
    }
}
