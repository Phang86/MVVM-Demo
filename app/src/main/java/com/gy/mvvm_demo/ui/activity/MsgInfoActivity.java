package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.ui.bean.TestRvBean;

import java.io.Serializable;

public class MsgInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_info);

        TestRvBean msg_info = (TestRvBean) getIntent().getSerializableExtra("msg_info");

        Bundle bundle = getIntent().getExtras();
        TestRvBean msginfo = (TestRvBean) bundle.getSerializable("msg_info");

        if (msginfo != null){
            ((TextView)findViewById(R.id.tv_msg_time)).setText(msg_info.getTime());
            ((TextView)findViewById(R.id.tv_msg_info)).setText(msg_info.getMsgInfo());
        }
    }

}