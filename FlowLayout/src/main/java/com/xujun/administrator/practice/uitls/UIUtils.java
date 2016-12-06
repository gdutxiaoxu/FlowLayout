package com.xujun.administrator.practice.uitls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

import java.util.Random;

/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 *
 * @author xujun
 * @time 2016/6/25 11:23.
 */
public class UIUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);//加上0.5f 为了四舍五入
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取随机的颜色
     *
     * @return
     */
    public static int getColor() {
        Random random = new Random();
        int red = random.nextInt(200) + 20;//范围[20,220)，以防出现黑色和白色，看不清的情况
        int green = random.nextInt(200) + 20;
        int blue = random.nextInt(200) + 20;
        return Color.rgb(red, green, blue);
    }

    /**
     * 获取指定颜色的圆角背景shape
     *
     * @return
     */
    public static Drawable createShape(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setCornerRadius(15);
        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    /**
     * 获取一个状态选择器
     *
     * @param pressed 按下的图案
     * @param normal  正常的图案
     * @return
     */
    public static StateListDrawable getStateListDrawable(Drawable pressed, Drawable normal) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{}, normal);
        return stateListDrawable;
    }


}
