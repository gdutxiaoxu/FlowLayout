package com.xujun.administrator.practice.view;



import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 * @author xujun
 * @time 2016/6/20 23:49.
 */
public class SimpleFlowLayout extends ViewGroup {
    private int  verticalSpacing = 20;

    public SimpleFlowLayout(Context context ) {
        super(context);
    }

    /**
     * 重写onMeasure方法是为了确定最终的大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
//处理Padding属性，让当前的ViewGroup支持Padding
        int widthUsed = paddingLeft + paddingRight;
        int heightUsed = paddingTop + paddingBottom;

        int childMaxHeightOfThisLine = 0;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
//                已用的宽度
                int childUsedWidth = 0;
//                已用的高度
                int childUsedHeight = 0;
//                调用ViewGroup自身的方法测量孩子的宽度和高度，我们也可以自己根据MeasureMode来测量
                measureChild(child,widthMeasureSpec,heightMeasureSpec);
                childUsedWidth += child.getMeasuredWidth();
                childUsedHeight += child.getMeasuredHeight();
//处理Margin，支持孩子的Margin属性
                Rect marginRect = getMarginRect(child);
                int leftMargin=marginRect.left;
                int rightMargin=marginRect.right;
                int topMargin=marginRect.top;
                int bottomMargin=marginRect.bottom;

                childUsedWidth += leftMargin + rightMargin;
                childUsedHeight += topMargin + bottomMargin;
//总宽度没有超过本行
                if (widthUsed + childUsedWidth < widthSpecSize) {
                    widthUsed += childUsedWidth;
                    if (childUsedHeight > childMaxHeightOfThisLine) {
                        childMaxHeightOfThisLine = childUsedHeight;
                    }
                } else {//总宽度已经超过本行
                    heightUsed += childMaxHeightOfThisLine + verticalSpacing;
                    widthUsed = paddingLeft + paddingRight + childUsedWidth;
                    childMaxHeightOfThisLine = childUsedHeight;
                }

            }

        }
//加上最后一行的最大高度
        heightUsed += childMaxHeightOfThisLine;
        setMeasuredDimension(widthSpecSize, heightUsed);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        /**
         * 为了 支持Padding属性
         */
        int childStartLayoutX = paddingLeft;
        int childStartLayoutY = paddingTop;

        int widthUsed = paddingLeft + paddingRight;

        int childMaxHeight = 0;

        int childCount = getChildCount();
//摆放每一个孩子的高度
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childNeededWidth, childNeedHeight;
                int left, top, right, bottom;

                int childMeasuredWidth = child.getMeasuredWidth();
                int childMeasuredHeight = child.getMeasuredHeight();

                Rect marginRect = getMarginRect(child);
                int leftMargin=marginRect.left;
                int rightMargin=marginRect.right;
                int topMargin=marginRect.top;
                int bottomMargin=marginRect.bottom;
                childNeededWidth = leftMargin + rightMargin + childMeasuredWidth;
                childNeedHeight = topMargin + topMargin + childMeasuredHeight;

//                没有超过本行
                if (widthUsed + childNeededWidth <= r - l) {
                    if (childNeedHeight > childMaxHeight) {
                        childMaxHeight = childNeedHeight;
                    }
                    left = childStartLayoutX + leftMargin;
                    top = childStartLayoutY + topMargin;
                    right = left + childMeasuredWidth;
                    bottom = top + childMeasuredHeight;
                    widthUsed += childNeededWidth;
                    childStartLayoutX += childNeededWidth;
                } else {
                    childStartLayoutY += childMaxHeight + verticalSpacing;
                    childStartLayoutX = paddingLeft;
                    widthUsed = paddingLeft + paddingRight;
                    left = childStartLayoutX + leftMargin;
                    top = childStartLayoutY + topMargin;
                    right = left + childMeasuredWidth;
                    bottom = top + childMeasuredHeight;
                    widthUsed += childNeededWidth;
                    childStartLayoutX += childNeededWidth;
                    childMaxHeight = childNeedHeight;
                }
                child.layout(left, top, right, bottom);
            }
        }
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

}