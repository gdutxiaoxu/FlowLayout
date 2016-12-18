package com.xujun.administrator.practice.uitls;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author xujun  on 2016/12/18.
 * @email gdutxiaoxu@163.com
 */

public class MyViewUtils {

    @NonNull
    public static TextView generateTextView(final String content, final Context context) {
        Drawable pressed = UIUtils.createShape(Color.GRAY);//被按下的颜色
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        Drawable normal = UIUtils.createShape(UIUtils.getColor());//正常的颜色
        StateListDrawable stateListDrawable = UIUtils.getStateListDrawable(pressed, normal);
        //状态选择器
        textView.setBackgroundDrawable(stateListDrawable);
        int textViewPadding = UIUtils.dip2px(context, 5);
        textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            }
        });
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup
                .MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.rightMargin=10;
        params.leftMargin=10;
        textView.setLayoutParams(params);
        return textView;
    }
}
