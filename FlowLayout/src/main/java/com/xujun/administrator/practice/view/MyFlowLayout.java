package com.xujun.administrator.practice.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xujun.administrator.practice.MySampleActivity;
import com.xujun.administrator.practice.R;
import com.xujun.administrator.practice.uitls.StringUtils;
import com.xujun.administrator.practice.uitls.UIUtils;

import java.util.List;

/**
 * @author xujun  on 2016/12/17.
 * @email gdutxiaoxu@163.com
 */

public class MyFlowLayout extends ViewGroup {

    public static final String TAG = "xujun";

    public MyFlowLayout(Context context) {
        super(context);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingTop();

        Log.i(TAG, "onMeasure: width=" + width);
        Log.i(TAG, "onMeasure: height=" + height);

        int useWidth = 0;
        int useHeight = 0;
        int maxHeightOfThisLine = 0;

        Rect rect;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == View.GONE) {
                continue;
            }
            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            rect = new Rect();
            getParamsRect(rect, view.getLayoutParams());
            useWidth += measuredWidth + rect.left + rect.right;

            if (useWidth > width) {//需要 换行了
                useWidth =measuredWidth + rect.left + rect.right;
                useHeight += maxHeightOfThisLine;
                maxHeightOfThisLine = measuredHeight;
            } else {
                if (maxHeightOfThisLine < measuredHeight) {
                    maxHeightOfThisLine = measuredHeight;
                }
            }

        }
        useHeight += maxHeightOfThisLine;
        useHeight = (heightMode == MeasureSpec.EXACTLY ? height : useHeight);

        Log.i(TAG, "onMeasure: width=" + width);
        Log.i(TAG, "onMeasure: useHeight=" + useHeight);

        setMeasuredDimension(width+getPaddingLeft()+getPaddingRight(),
                useHeight+getPaddingTop()+getPaddingBottom());

    }

    private void getParamsRect(Rect rect, LayoutParams layoutParams) {
        if (layoutParams instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;

            rect.left = marginLayoutParams.leftMargin;
            rect.top = marginLayoutParams.topMargin;
            rect.right = marginLayoutParams.rightMargin;
            rect.bottom = marginLayoutParams.bottomMargin;

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int useWidth = 0;
        int useHeight = 0;
        int maxHeightOfThisLine = 0;
        int childLeft = 0;
        int childRight = 0;
        int childTop = 0;
        int childBottom = 0;
        Rect rect;
        int width = getMeasuredWidth()-getPaddingLeft()-getPaddingRight();

        int height = getMeasuredHeight()-getPaddingTop()-getPaddingBottom();
        Log.i(TAG, "onLayout: width=" +width);
        Log.i(TAG, "onLayout: height=" +height);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == View.GONE) {
                continue;
            }

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();


            rect = new Rect();
            getParamsRect(rect, view.getLayoutParams());
            useWidth += measuredWidth + rect.left + rect.right;

            if (useWidth > width) {//需要 换行了
                useWidth =measuredWidth + rect.left + rect.right;
                childLeft = rect.left;
                childRight = childLeft + measuredWidth;
                useHeight += maxHeightOfThisLine;
                childTop = useHeight;
                childBottom = childTop + measuredHeight;

                maxHeightOfThisLine = measuredHeight;


            } else {
                childLeft = useWidth - measuredWidth;
                childRight = childLeft + measuredWidth;
                childTop = useHeight;
                childBottom = childTop + measuredHeight;
                if (maxHeightOfThisLine < measuredHeight) {
                    maxHeightOfThisLine = measuredHeight;
                }

            }
            view.layout(childLeft, childTop, childRight, childBottom);

        }
    }

    @NonNull
    private TextView generateTextView(final String content) {
        Drawable pressed = UIUtils.createShape(Color.GRAY);//被按下的颜色
        TextView textView = new TextView(getContext());
        textView.setText(content);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        Drawable normal = UIUtils.createShape(UIUtils.getColor());//正常的颜色
        StateListDrawable stateListDrawable = UIUtils.getStateListDrawable(pressed, normal);
        //状态选择器
        textView.setBackgroundDrawable(stateListDrawable);
        int textViewPadding = UIUtils.dip2px(getContext(), 5);
        textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
        textView.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
        return textView;
    }

    public void initView() {


        List<String> datas = StringUtils.getDatas();
        for (int i = 0; i < datas.size(); i++) {
            TextView textView = generateTextView(datas.get(i));
            this.addView(textView);

        }

    }
}
