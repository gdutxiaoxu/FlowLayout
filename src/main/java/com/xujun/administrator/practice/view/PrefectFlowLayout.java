package com.xujun.administrator.practice.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 * @author xujun
 * @time 2016/6/26 22:54.
 */
public class PrefectFlowLayout extends ViewGroup {


    public PrefectFlowLayout(Context context) {
        super(context);
    }

    public PrefectFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefectFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int parentWidthSize;//父容器宽度
    private int horizontalSpacing = 16;//水平间距
    private int verticalSpacing = 16;//垂直间距
    private Line currentLine;//当前行
    private List<Line> mLines = new ArrayList<>();//所有行的集合
    private int userWidth = 0;//当前行已使用宽度

    /**
     * 行对象
     */
    private class Line {
        private List<View> children;//一行里面所添加的子View集合
        private int height;//当前行高度
        private int lineWidth = 0;//当前行已使用宽度

        public Line() {
            children = new ArrayList<>();
        }

        /**
         * 添加一个子控件
         *
         * @param child
         */
        private void addChild(View child) {
            children.add(child);
            if (child.getMeasuredHeight() > height) {
                height = child.getMeasuredHeight();//当前行高度以子控件最大高度为准
            }
            lineWidth += child.getMeasuredWidth();//将每个子控件宽度进行累加，记录使用的宽度
        }

        /**
         * 获取行的高度
         *
         * @return
         */
        public int getHeight() {
            return height;
        }

        /**
         * 获取子控件的数量
         *
         * @return
         */
        public int getChildCount() {
            return children.size();
        }

        /**
         * 放置每一行里面的子控件的位置
         *
         * @param l 距离最左边的距离
         * @param t 距离最顶端的距离
         */
        public void onLayout(int l, int t) {
            //当前行使用的宽度，等于每个子控件宽度之和+子控件之间的水平距离
            lineWidth += horizontalSpacing * (children.size() - 1);
            int surplusChild = 0;
            int surplus = parentWidthSize - lineWidth;//剩余宽度
            if (surplus > 0) {
                //如果有剩余宽度，则将剩余宽度平分给每一个子控件
                surplusChild = (int) (surplus / children.size()+0.5);
            }
            for (int i = 0; i < children.size(); i++) {
                View child = children.get(i);
                child.getLayoutParams().width=child.getMeasuredWidth()+surplusChild;
                if (surplusChild>0){//如果长度改变了后，需要重新测量，否则布局中的属性大小还会是原来的大小
                    child.measure(MeasureSpec.makeMeasureSpec(child.getMeasuredWidth()+surplusChild,MeasureSpec.EXACTLY)
                            ,MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY));
                }
                child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
                l += child.getMeasuredWidth() + horizontalSpacing;
            }
        }
    }
    //  getMeasuredWidth()   控件实际的大小
    // getWidth()  控件显示的大小

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //将之前测量的数据进行清空，以防复用时影响下次测量
        mLines.clear();
        currentLine = null;
        userWidth = 0;
        //获取父容器的宽度和模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        parentWidthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //获取父容器的高度和模式
        int heigthMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int childWidthMode, childHeightMode;
        //为了测量每个子控件，需要指定每个子控件的测量规则
        //子控件设置为WRAP_CONTENT，具体测量规则详见，ViewGroup的getChildMeasureSpec()方法
        if (widthMode == MeasureSpec.EXACTLY) {
            childWidthMode = MeasureSpec.AT_MOST;
        } else {
            childWidthMode = widthMode;
        }
        if (heigthMode == MeasureSpec.EXACTLY) {
            childHeightMode = MeasureSpec.AT_MOST;
        } else {
            childHeightMode = heigthMode;
        }
        //获取到子控件高和宽的测量规则
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(parentWidthSize, childWidthMode);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, childHeightMode);
        currentLine = new Line();//创建第一行
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //测量每一个孩子
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            int childMeasuredWidth = child.getMeasuredWidth();//获取当前子控件的实际宽度
            userWidth += childMeasuredWidth;//让当前行使用宽度加上当前子控件宽度
            if (userWidth <= parentWidthSize) {
                //如果当前行使用宽度小于父控件的宽度，则加入该行
                currentLine.addChild(child);
                userWidth += horizontalSpacing;//当前行使用宽度加上子控件之间的水平距离
                if (userWidth > parentWidthSize) {//如果当前行加上水平距离后超出父控件宽度,则换行
                    newLine();
                }
            } else {
                if (currentLine.getChildCount() == 0) {//以防出现一个子控件宽度超过父控件的情况出现
                    currentLine.addChild(child);
                }
                newLine();
                currentLine.addChild(child);//并将超出范围的当前的子控件加入新的行中
                userWidth = child.getMeasuredWidth()+horizontalSpacing;//并将使用宽度加上子控件的宽度;
            }
        }
        if (!mLines.contains(currentLine)) {//加入最后一行，因为如果最后一行宽度不足父控件宽度时，就未换行
            mLines.add(currentLine);
        }
        int totalHeight = 0;//总高度
        for (Line line : mLines) {
            totalHeight += line.getHeight() + verticalSpacing;//总高度等于每一行的高度+垂直间距
        }
        setMeasuredDimension(parentWidthSize + getPaddingLeft() + getPaddingRight(),
                resolveSize(totalHeight + getPaddingTop() + getPaddingBottom(), heightMeasureSpec));//resolveSize(),将实际高度与父控件高度进行比较，选取最合适的
    }

    /**
     * 换行
     */
    private void newLine() {
        mLines.add(currentLine);//记录之前行
        currentLine = new Line();//重新创建新的行
        userWidth = 0;//将使用宽度初始化
    }

    /**
     * 放置每个子控件的位置
     *
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l += getPaddingLeft();
        t += getPaddingTop();
        for (int i = 0; i < mLines.size(); i++) {
            Line line = mLines.get(i);
            line.onLayout(l, t);//设置每一行的位置，每一行的子控件由其自己去分配
            t += line.getHeight() + verticalSpacing;//距离最顶端的距离，即每一行高度和垂直间距的累加
        }
    }

    /**
     * 获取子控件的测量规则
     *
     * @param mode 父控件的测量规则
     * @return 子控件设置为WRAP_CONTENT，具体测量规则详见，ViewGroup的getChildMeasureSpec()方法
     */
    private int getMode(int mode) {
        int childMode = 0;
        if (mode == MeasureSpec.EXACTLY) {
            childMode = MeasureSpec.AT_MOST;
        } else {
            childMode = mode;
        }
        return childMode;
    }
}
