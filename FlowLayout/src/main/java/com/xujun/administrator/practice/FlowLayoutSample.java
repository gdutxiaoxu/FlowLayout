package com.xujun.administrator.practice;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xujun.administrator.practice.uitls.StringUtils;
import com.xujun.administrator.practice.uitls.UIUtils;
import com.xujun.administrator.practice.view.FlowLayout2;

import java.util.List;

public class FlowLayoutSample extends AppCompatActivity {

    private FlowLayout2 mFlowLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout_sample);
        initView();
    }

    private void initView() {
        mFlowLayout2 = (FlowLayout2) findViewById(R.id.flow_layout2);
        List<String> datas = StringUtils.getDatas();
        for (int i = 0; i < datas.size(); i++) {
            TextView textView = generateTextView(datas.get(i));
            mFlowLayout2.addView(textView);
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
                Toast.makeText(FlowLayoutSample.this, content, Toast.LENGTH_SHORT).show();
            }
        });
        textView.setLayoutParams(
                new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.MarginLayoutParams.WRAP_CONTENT));
        return textView;
    }
}
