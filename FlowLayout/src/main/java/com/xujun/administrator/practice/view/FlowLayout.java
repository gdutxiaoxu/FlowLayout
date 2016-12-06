package com.xujun.administrator.practice.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @ explain:
 * @ author：xujun on 2016/6/25 22:21
 * @ email：gdutxiaoxu@163.com
 */
public class FlowLayout extends ViewGroup {

    private int mWidth = 0;
    private Line mCurrentLine;
    /**
     * 默认的水平距离
     */
    private final int mDefaultXSpace = 5;
    private int mXSpace = mDefaultXSpace;
    /**
     * 默认的垂直距离
     */
    private int mYspace = 10;

    private List<Line> mLineList = new ArrayList<>();
    private String TAG="xujun";

    public FlowLayout(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Logger.i("onMeasure");
        mCurrentLine = null;
        mLineList.clear();
        mWidth = MeasureSpec.getSize(widthMeasureSpec);

        int childCount = getChildCount();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();

        /**
         * 总共已用的宽度，换行以后需要重新赋值
         */
        int usedWidth = paddingLeft + paddingRight+mXSpace;
        /**
         * 总共已用的高度
         */
        int usedHeight = getPaddingBottom() + getPaddingTop();
        /**
         * 该行最大的 高度
         * 每次添加一个child需要重新 比较赋值
         * 1）没有换行
         * 2）换行以后需要重新赋值
         *
         */
        int childMaxHeightOfThisLine = 0;
        mCurrentLine = new Line();
        for (int i = 0; i < childCount; i++) {

            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childMeasuredHeight = child.getMeasuredHeight();
            int childMeasuredWidth = child.getMeasuredWidth();
            Rect marginRect = getMarginRect(child);
            int childUsedWidth = childMeasuredWidth + marginRect.left + marginRect.right + mXSpace;

//            没有 超过本行
            if (usedWidth + childUsedWidth < mWidth) {
                if (childMeasuredHeight > childMaxHeightOfThisLine) {
                    childMaxHeightOfThisLine = childMeasuredHeight;
                }
                usedWidth += childUsedWidth;
                mCurrentLine.addView(child);
                if (!mLineList.contains(mCurrentLine)) {
                    mLineList.add(mCurrentLine);
                }
            } else {
                usedWidth = paddingLeft + paddingRight + childUsedWidth+mXSpace;
                usedHeight += childMaxHeightOfThisLine + marginRect.top + marginRect.bottom +
                        mYspace;
                childMaxHeightOfThisLine = childMeasuredHeight;
                mCurrentLine = newLine();
                mCurrentLine.addView(child);
            }
        }
        usedHeight += childMaxHeightOfThisLine + mYspace+childMaxHeightOfThisLine;
        Log.i(TAG, "onMeasure: usedHeight="+usedHeight);
        setMeasuredDimension(mWidth, usedHeight);
    }

    private Line newLine() {
        if (!mLineList.contains(mCurrentLine)) {
            mLineList.add(mCurrentLine);
        }
        return new Line();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed == false) {
            return;
        }

        int left = l+getPaddingLeft();
        int top = t+getPaddingTop() ;
        int totalHeight=0;
        Log.i(TAG, "onLayout: top"+top);
        for (int i = 0; i < mLineList.size(); i++) {
            Line line = mLineList.get(i);
            totalHeight+=line.mLineHeight+mYspace;
            if(i==mLineList.size()-1){
                Log.i(TAG, "onLayout: top"+top);
            }
            line.onLayout(left, top);
            top += line.mLineHeight + mYspace;
        }
        Log.i(TAG, "onLayout: totalHeight="+totalHeight);

    }

    private Rect getMarginRect(View child) {
        LayoutParams layoutParams = child.getLayoutParams();
        int leftMargin = 0;
        int rightMargin = 0;
        int topMargin = 0;
        int bottomMargin = 0;
        if (layoutParams instanceof MarginLayoutParams) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) layoutParams;
            leftMargin = marginLayoutParams.leftMargin;
            rightMargin = marginLayoutParams.rightMargin;
            topMargin = marginLayoutParams.topMargin;
            bottomMargin = marginLayoutParams.bottomMargin;

        }
        return new Rect(leftMargin, topMargin, rightMargin, bottomMargin);
    }

    private class Line {

        List<View> mViewList;

        private int mLineHeight;
        private int mLineWidth;

        private int mSurplusWidth = 0;

        public Line() {
            mViewList = new ArrayList<View>();
            ;
        }

        public void addView(View child) {
            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();
            Rect marginRect = getMarginRect(child);
            if (childHeight > mLineHeight) {
                mLineHeight = childHeight;
            }
            mLineWidth += childWidth + mXSpace + marginRect.left + marginRect.right;
            mViewList.add(child);
        }

        void onLayout(int left, int top) {
            mSurplusWidth = mWidth - mLineWidth;
            int size = mViewList.size();
//            强行剩余的距离平均分配 给mXSpace；
            if (mSurplusWidth > 0) {
                mXSpace += mSurplusWidth / (size + 1);
            }

            Log.i("onLayout", "----------------------------------------------");
            left += mXSpace;
            for (int i = 0; i < size; i++) {
                View child = mViewList.get(i);
                Rect marginRect = getMarginRect(child);
                int childMeasuredWidth = child.getMeasuredWidth();
                int childMeasuredHeight = child.getMeasuredHeight();
                int childUsedWidth = childMeasuredWidth + marginRect.left + marginRect.right +
                        mXSpace;

                int bottom = top + childMeasuredHeight;
                int right = left + childMeasuredWidth;
                Log.i("onLayout", "(" + left + "," + right + "," + top + "," + bottom + ")");
                child.layout(left, top, right, bottom);
                left += childUsedWidth;


            }
            mXSpace = mDefaultXSpace;
            Log.i(TAG, "onLayout: left="+left);

        }

    }
}
