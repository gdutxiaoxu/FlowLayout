package com.xujun.administrator.practice;


/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 *
 * @author xujun
 * @time 2016/6/24 16:56.
 */

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xujun.administrator.practice.uitls.StringUtils;
import com.xujun.administrator.practice.uitls.UIUtils;
import com.xujun.administrator.practice.view.PrefectFlowLayout;
import com.xujun.administrator.practice.view.SimpleFlowLayout;

import java.util.List;

public class PrefectSampleActivity extends AppCompatActivity {

    private FrameLayout mRoot;
    private String Tag = "xujun";
    private SimpleFlowLayout mSimpleFlowLayout;
    private PrefectFlowLayout mPrefectFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefect_sample);
        mRoot = (FrameLayout) findViewById(R.id.root);


    }

    public void onButtonClick(View v) {
        switch (v.getId()) {
            case R.id.btn_1:
                if (mSimpleFlowLayout == null) {
                    mSimpleFlowLayout = new SimpleFlowLayout(PrefectSampleActivity.this);
                    List<String> datas = StringUtils.getDatas();
                    for (int i = 0; i < datas.size(); i++) {
                        TextView textView = generateTextView(datas.get(i));
                        mSimpleFlowLayout.addView(textView);
                    }
                }
                if (mRoot.getChildCount() >= 1) {
                    mRoot.removeAllViews();
                }
                mRoot.addView(mSimpleFlowLayout);

                break;
            case R.id.btn_2:
                if (mPrefectFlowLayout == null) {
                    mPrefectFlowLayout = new PrefectFlowLayout(PrefectSampleActivity.this);
                    List<String> datas = StringUtils.getDatas();
                    for (int i = 0; i < datas.size(); i++) {
                        TextView textView = generateTextView(datas.get(i));
                        mPrefectFlowLayout.addView(textView);
                    }
                }
                if (mRoot.getChildCount() >= 1) {
                    mRoot.removeAllViews();
                }
                mRoot.addView(mPrefectFlowLayout);

                break;
            default:
                break;
        }
    }

    @NonNull
    private TextView generateTextView(final String content) {
        Drawable pressed = UIUtils.createShape(Color.GRAY);//被按下的颜色
        TextView textView = new TextView(this);
        textView.setText(content);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        textView.setGravity(Gravity.CENTER);
        Drawable normal = UIUtils.createShape(UIUtils.getColor());//正常的颜色
        StateListDrawable stateListDrawable = UIUtils.getStateListDrawable(pressed, normal);
        //状态选择器
        textView.setBackgroundDrawable(stateListDrawable);
        int textViewPadding = UIUtils.dip2px(this, 5);
        textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PrefectSampleActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
        textView.setLayoutParams(
                new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.MarginLayoutParams.WRAP_CONTENT));
        return textView;
    }


}
