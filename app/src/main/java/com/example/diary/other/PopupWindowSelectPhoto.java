package com.example.diary.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import com.example.diary.R;

/**
 * Created by 周要明 on 2017/8/7.
 */

public class PopupWindowSelectPhoto extends PopupWindow {
    private View view;
    private Button takePhoto,album,cancel;
    public PopupWindowSelectPhoto(Context context, View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.popup_window_layout,null);
        takePhoto = (Button)view.findViewById(R.id.take_photo);
        album = (Button)view.findViewById(R.id.album);
        cancel = (Button)view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        takePhoto.setOnClickListener(itemsOnClick);
        album.setOnClickListener(itemsOnClick);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setAnimationStyle(R.style.Animation);
        ColorDrawable colorDrawable = new ColorDrawable(0x30000000);
        this.setBackgroundDrawable(colorDrawable);
    }
}