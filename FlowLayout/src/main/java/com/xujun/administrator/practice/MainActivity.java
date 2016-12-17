package com.xujun.administrator.practice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View v){
        switch (v.getId()){
            case R.id.btn_flowLayot:
                jump(FlowLayoutSample.class);
                break;

            case R.id.btn_PrefectLayot:
                jump(PrefectSampleActivity.class);
                break;

            case R.id.btn_MyFlowLayot:
                jump(MySampleActivity.class);
                break;

            default:break;
        }
    }

    private <AT extends Activity> void jump(Class<AT> clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }
}
