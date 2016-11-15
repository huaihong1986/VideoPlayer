package com.cvn.cmsgd.player;


import android.content.Context;
import android.util.AttributeSet;

public class FitVideoView extends VideoView {
    public FitVideoView(Context context) {
        super(context);
    }

    public FitVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
}