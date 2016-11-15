package com.cvn.cmsgd.util;

import android.content.Context;

/**
 * Created by keda on 2016/3/24.
 */
public class DensityUtil {

    /**
     * According to the resolution of the mobile phone change dp(dip) to px (pixels)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     *  According to the resolution of the mobile phone change px (pixels) to dp(dip)
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
