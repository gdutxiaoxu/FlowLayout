package com.xujun.administrator.practice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xujun.administrator.practice.uitls.MyViewUtils;
import com.xujun.administrator.practice.uitls.StringUtils;
import com.xujun.administrator.practice.view.MyPrefectLayout;

import java.util.List;

public class MyPrefectSample extends AppCompatActivity {

    private MyPrefectLayout mMyPrefectLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_prefect_sample);
        initView();
    }

    private void initView() {
        mMyPrefectLayout = (MyPrefectLayout) findViewById(R.id.my_prefect_layout);
        List<String> datas = StringUtils.getDatas();
        for (int i = 0; i < datas.size(); i++) {
            TextView textView = MyViewUtils.generateTextView(datas.get(i),
                    MyPrefectSample.this);
            mMyPrefectLayout.addView(textView);
        }
    }


}
