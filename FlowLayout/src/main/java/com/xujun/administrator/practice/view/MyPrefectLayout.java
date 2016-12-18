package com.xujun.administrator.practice.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static com.xujun.administrator.practice.view.MyFlowLayout.TAG;

/**
 * @author xujun  on 2016/12/17.
 * @email gdutxiaoxu@163.com
 */

public class MyPrefectLayout extends ViewGroup {

    int mVerticalDistance = 15;
    List<Line> mLines = new ArrayList<>();
    private int mMaxWidth;

    public MyPrefectLayout(Context context) {
        super(context);
    }

    public MyPrefectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyPrefectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLines.clear();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingTop();
        mMaxWidth=width;
        Rect rect = new Rect();
        int useWidth = 0;
        int useHeight = 0;
        int maxHeightOfThisLine = 0;
        Line line = new Line();
        mLines.add(line);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            getParamsRect(rect, child.getLayoutParams());
           /* measureChild(child, widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = child.getMeasuredWidth();
            if(measuredWidth+ rect.left + rect.right>mMaxWidth){
                //如果长度改变了后，需要重新测量，否则布局中的属性大小还会是原来的大小
                child.measure(MeasureSpec.makeMeasureSpec(
                        mMaxWidth-rect.left-rect.right,MeasureSpec.EXACTLY)
                        ,MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(),MeasureSpec.EXACTLY));
                measuredWidth=child.getMeasuredWidth();
            }*/
            measureChildWithMargins(child,widthMeasureSpec,
                    rect.left+rect.right,heightMeasureSpec,0);
            int measuredWidth = child.getMeasuredWidth();


            int measuredHeight = child.getMeasuredHeight();
            useWidth += measuredWidth + rect.left + rect.right;
            if (useWidth > width) {//换行了
//                设置上一行已用的宽度
                line.setLineUseWidth(useWidth-measuredWidth-rect.left-rect.right);

                useWidth = measuredWidth + rect.left + rect.right;
                useHeight += maxHeightOfThisLine + mVerticalDistance;
                maxHeightOfThisLine = measuredHeight;
                //   重新另起一行
               ;
                line = newLine();
                line.setLineMaxHeight(maxHeightOfThisLine);


            } else {

                if (maxHeightOfThisLine < measuredHeight) {
                    maxHeightOfThisLine = measuredHeight;
                    line.setLineMaxHeight(maxHeightOfThisLine);
                }
            }
            //            将孩子添加到Line中
            line.addView(child);

        }
        line.setLineUseWidth(useWidth);
        useHeight += maxHeightOfThisLine + mVerticalDistance;
        useHeight = (heightMode == MeasureSpec.EXACTLY ? height + getPaddingTop() +
                getPaddingBottom() : useHeight);
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), useHeight +
                getPaddingBottom() + getPaddingTop());
    }

    @NonNull
    private Line newLine() {
        Line line = new Line();
        mLines.add(line);
        return line;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        int top = getTop()+getPaddingTop();
        int left=getLeft()+getPaddingLeft();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            if(i==0){
                top = getTop()+getPaddingTop();
            }else{
                top += mLines.get(i-1).mLineMaxHeight +mVerticalDistance;
            }

            line.onLayout(left,top);
        }

    }

    private class Line {
        private List<View> mViews;
        private int mLineMaxHeight;
        private int mLineMaxWidth;
        private  int mLineUseWidth =0;
        private int useHeight;
        private int remainderWidth;
        boolean isAverage = true;

        public void setLineUseWidth(int lineUseWidth){
            this.mLineUseWidth = lineUseWidth;
            if(mLineUseWidth>mLineMaxWidth){
                mLineUseWidth=mLineMaxWidth;
            }
        }

        public void setLineMaxHeight(int lineMaxHeight) {
            this.mLineMaxHeight = lineMaxHeight;
        }

        public void addView(View view) {
            mViews.add(view);
        }

        public Line() {
            mViews = new ArrayList<>();
            this.mLineMaxWidth = mMaxWidth;
        }

        public void onLayout(int left,int top) {
            int useWidth = left;
            int childLeft = 0;
            int childRight = 0;
            int childTop = top;
            int childBottom = 0;
            int size = mViews.size();
            Log.i(TAG, "onLayout: mLineUseWidth=" +mLineUseWidth);
            remainderWidth=(mLineMaxWidth-mLineUseWidth)/size;
            Log.i(TAG, "onLayout: remainderWidth=" +remainderWidth);
            int paddingLeft = remainderWidth / size;
            int yushu = remainderWidth % size;
            int paddingRight = paddingLeft;
            Rect rect = new Rect();
            for (int i = 0; i < size; i++) {
                View view = mViews.get(i);

                if (isAverage) {
                    if (remainderWidth>0){
                        //如果长度改变了后，需要重新测量，否则布局中的属性大小还会是原来的大小
                        view.measure(MeasureSpec.makeMeasureSpec(
                                view.getMeasuredWidth()+remainderWidth,MeasureSpec.EXACTLY)
                                ,MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(),MeasureSpec.EXACTLY));
                    }

                } else {

                }
                int measuredWidth = view.getMeasuredWidth();
                int measureHeight = view.getMeasuredHeight();
                getParamsRect(rect, view.getLayoutParams());

                childLeft = useWidth + rect.left;
                childRight = childLeft + measuredWidth;
                childTop = top;
                childBottom = top + measureHeight;
                useWidth += measuredWidth + rect.left + rect.right;

                view.layout(childLeft, childTop, childRight, childBottom);


            }
        }
    }

    private void getParamsRect(Rect rect, LayoutParams layoutParams) {
        if (layoutParams instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;

            rect.left = marginLayoutParams.leftMargin;
            rect.top = marginLayoutParams.topMargin;
            rect.right = marginLayoutParams.rightMargin;
            rect.bottom = marginLayoutParams.bottomMargin;

        } else {
            rect.left = 0;
            rect.right = 0;
            rect.top = 0;
            rect.bottom = 0;
        }
    }
}
