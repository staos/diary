package com.example.diary.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diary.R;;
import com.example.diary.bean.Comment;
import com.example.diary.bean.Mood;
import com.example.diary.bean._User;
import com.example.diary.other.MyApplication;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 周要明 on 2017/8/18.
 */

public class moodAdapter extends ArrayAdapter<Mood> {
    private static final String TAG = "moodAdapter";
    private int resourceId;
    private View.OnClickListener itemOnClick;
    _User myUser = _User.getCurrentUser(_User.class);
    String myUserId = myUser.getObjectId();
    private List<Comment> mComment = new ArrayList<>();
    public moodAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Mood> objects, List<Comment> comments, View.OnClickListener itemsOnClick) {
        super(context, resource, objects);
        resourceId = resource;
        itemOnClick = itemsOnClick;
        mComment = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final _User user = _User.getCurrentUser(_User.class);
        final Mood mood = getItem(position);//取得当前项的Mood实例
        View view;
        final ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.headImage = (CircleImageView)view.findViewById(R.id.userImage);
            viewHolder.nickname = (TextView)view.findViewById(R.id.userName);
            viewHolder.content = (TextView) view.findViewById(R.id.myContent);
            viewHolder.starButton = (LikeButton)view.findViewById(R.id.star_button);
            viewHolder.commentButton = (ImageView)view.findViewById(R.id.comment);
            viewHolder.likeButton = (LikeButton)view.findViewById(R.id.like_button);
            viewHolder.likeNumber = (TextView)view.findViewById(R.id.likeNumber);
            viewHolder.commentLayout = (LinearLayout)view.findViewById(R.id.commentLayout);
            viewHolder.commentText = (TextView)view.findViewById(R.id.comment_text);
            view.setTag(viewHolder);//将viewHolder存储在view中
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        //给控件填充数据
        Picasso.with(MyApplication.getContext())
                .load(mood.getAuthor().getHeadImageUrl())
                .into(viewHolder.headImage);
        viewHolder.nickname.setText(mood.getAuthor().getNickName());
        viewHolder.content.setText(mood.getContent());
        viewHolder.commentButton.setTag(position);
        viewHolder.commentButton.setOnClickListener(itemOnClick);//评论按钮
        viewHolder.starButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                BmobRelation relation = new BmobRelation();
                relation.add(myUser);
                mood.setCollect(relation);
                mood.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Log.d(TAG, "收藏信息更新成功");
                        }else{
                            Log.d(TAG, "收藏信息更新失败 " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                BmobRelation relation =  new BmobRelation();
                relation.remove(myUser);
                mood.setCollect(relation);
                mood.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Log.d(TAG, "取消收藏成功");
                        }else{
                            Log.d(TAG, "取消收藏失败 " + e.getMessage());
                        }
                    }
                });
            }
        });
        viewHolder.likeButton.setOnLikeListener(new OnLikeListener() {//点赞按钮的监听
            @Override
            public void liked(LikeButton likeButton) {
                BmobRelation relation = new BmobRelation();
                relation.add(myUser);
                mood.setLikes(relation);
                viewHolder.likeNumber.setText(String.valueOf(Integer.valueOf(viewHolder.likeNumber.getText().toString()).intValue() + 1));
                mood.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Log.d(TAG, "点赞信息更新成功");
                        }else{
                            Log.d(TAG, "点赞信息更新失败 " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                viewHolder.likeNumber.setText(String.valueOf(Integer.valueOf(viewHolder.likeNumber.getText().toString()).intValue() - 1));
                BmobRelation relation = new BmobRelation();
                relation.remove(myUser);
                mood.setLikes(relation);
                mood.update(mood.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if ( e == null){
                            Log.d(TAG, "点赞信息更新成功");
                        }else{
                            Log.d(TAG, "点赞信息更新失败");
                        }
                    }
                });
            }
        });
        return view;
    }
    static class ViewHolder{
        CircleImageView headImage;
        TextView nickname;
        TextView content;
        LikeButton starButton;
        ImageView commentButton;
        LikeButton likeButton;
        TextView likeNumber;
        LinearLayout commentLayout;
        TextView commentText;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public Mood getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

}
