package com.homework.wucong.gobang.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.homework.wucong.gobang.R;

public class UserIcon extends View {

    Bitmap userIcon;
    Bitmap stateEdge;
    Context context;

    public UserIcon(Context context) {
        super(context);
        this.context = context;
    }

    public UserIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public UserIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public UserIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
    }

    public UserIcon(Context context,Bitmap bitmap) {
        super(context);
        this.context = context;
        userIcon = bitmap;
    }

    void init(){
        stateEdge = BitmapFactory.decodeResource(getResources(), R.drawable.headbox);
    }
}
