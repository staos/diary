package com.example.diary.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.diary.R;
import com.example.diary.adapter.moodAdapter;
import com.example.diary.bean.Comment;
import com.example.diary.bean.Mood;
import com.example.diary.bean._User;
import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 周要明 on 2017/8/16.
 */

public class WholeFragment extends Fragment implements PullToRefreshView.OnRefreshListener,View.OnTouchListener,AdapterView.OnItemClickListener{
    private static final String TAG = "WholeFragment";
    PullToRefreshView pullToRefreshView;
    private static final long REFRESH_DELAY = 1000;
    private List<Mood> moodList = new ArrayList<>();
    private List<Comment> commentList = new ArrayList<>();
    private moodAdapter adapter;
    private View view;
    private RefreshListView listView;
    private  View commentView = null;
    private PopupWindow commentPopup = null;
    private String result = "";
    private EditText comment_edit;
    private TextView comment_send;
    /**
     *item的高度
     */
    private int viewHeight;

    /**
     *当前点击的位置
     */
    private int position;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.whole_fragment,container,false);
        }
        pullToRefreshView = (PullToRefreshView)view.findViewById(R.id.whole_pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(this);
        importComment();
        listView = (RefreshListView) view.findViewById(R.id.wholeListView);
        listView.setOnItemClickListener(this);
        adapter = new moodAdapter(MyApplication.getContext(),R.layout.mood_item,moodList,commentList,itemOnClick);
        listView.setAdapter(adapter);
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshData();
                pullToRefreshView.setRefreshing(false);
            }
        },REFRESH_DELAY);
        return view;
    }

    @Override
    public void onRefresh() {
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                RefreshData();
                pullToRefreshView.setRefreshing(false);
            }
        },REFRESH_DELAY);
    }
    private void RefreshData(){
        BmobQuery<Mood> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.include("author");
        query.findObjects(new FindListener<Mood>() {
            @Override
            public void done(List<Mood> list, BmobException e) {
                if (e == null){
                    if (moodList != null){
                        moodList.clear();
                    }
                    for (Mood mood : list)
                        moodList.add(mood);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "数据加载成功");
                }else{
                    Log.d(TAG, "失败 " + e.getMessage());
                }
            }
        });
    }
    private View.OnClickListener itemOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            position = (int)v.getTag();
            Log.d(TAG, "onClick: " + position);
            liveCommentEdit(getActivity(),view,liveCommentResult);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Mood mood = moodList.get(position);

    }

    //评论
    public interface liveCommentResult {
        void onResult(boolean confirmed, String comment);
    }
    private MineFragment.liveCommentResult liveCommentResult = new MineFragment.liveCommentResult() {
        @Override
        public void onResult(boolean confirmed, String comment) {
            _User user = _User.getCurrentUser(_User.class);
            String nickName = user.getNickName();
            View view = listView.getChildAt(position - listView.getFirstVisiblePosition()).findViewById(R.id.moodItem);
            Log.d(TAG, "onResult: " + position);
            LinearLayout commentLayout = (LinearLayout)view.findViewById(R.id.commentLayout);
            commentLayout.setVisibility(View.VISIBLE);
            TextView commentText = (TextView)view.findViewById(R.id.comment_text);
            if (commentText.getText().toString().equals("")){
                commentText.setText(nickName + ": " + comment);
            }else{
                commentText.setText(commentText.getText() + "\n" + nickName + ": " + comment);
            }
            Comment com = new Comment();
            com.setContent(nickName + ": " +comment);
            com.setMood(adapter.getItem(position));
            com.setCommenter(user);
            com.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if ( e == null){
                        Log.d(TAG, "评论信息成功");
                    }else{
                        Log.d(TAG, "评论信息更新失败 " + e.getMessage());
                    }
                }
            });
        }
    };
    public  void liveCommentEdit(final Activity context, View view, final MineFragment.liveCommentResult commentResult) {
        liveCommentResult = commentResult;
        if (commentView == null) {
            commentView = context.getLayoutInflater().inflate(R.layout.comment_popupwindow, null);
        }
        if (commentPopup == null) {
            // 创建一个PopuWidow对象
            commentPopup = new PopupWindow(commentView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        // 设置动画 commentPopup.setAnimationStyle(R.style.popWindow_animation_in2out);
        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        commentPopup.setFocusable(true);
        // 设置允许在外点击消失
        commentPopup.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        commentPopup.setBackgroundDrawable(new BitmapDrawable());
        //必须加这两行，不然不会显示在键盘上方
        commentPopup.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        commentPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // PopupWindow的显示及位置设置
        commentPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        comment_edit = (EditText) commentView.findViewById(R.id.editComment);
        comment_send = (TextView) commentView.findViewById(R.id.ec_text);
        //这是布局中发送按钮的监听
        comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = comment_edit.getText().toString().trim();
                if (liveCommentResult != null && result.length() != 0) {
                    //把数据传出去
                    liveCommentResult.onResult(true, result);
                    //关闭popup
                    commentPopup.dismiss();
                }
            }
        });
        //设置popup关闭时要做的操作
        commentPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                hideSoftInput();
                comment_edit.setText("");
            }
        });
        //显示软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                showKeyboard();
            }
        }, 200);
    }
    private void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(comment_edit.getWindowToken(),0);
    }
    private void showKeyboard(){
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        comment_edit.requestFocus();
        imm.showSoftInput(comment_edit, 0);
    }
    private void importComment(){
        BmobQuery<Comment> query = new BmobQuery<>();
        query.order("-updatedAt");
        query.include("mood");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if (e == null){
                    if (commentList != null){
                        commentList.clear();
                    }
                    for (Comment comment : list)
                        commentList.add(comment);
                    Log.d(TAG, "数据加载成功");
                }else{
                    Log.d(TAG, "失败 " + e.getMessage());
                }
            }
        });
    }
}
