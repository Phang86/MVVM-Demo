package com.gy.mvvm_demo.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.utils.Md5Utils;
import com.gy.mvvm_demo.view.WifiStateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.ivWifi)
    ImageView ivWifi;
    @BindView(R.id.tvWifi)
    TextView tvWifi;
    private ImageView ivSearch, ivClose;
    private EditText edSearch;
    private RelativeLayout laySearch;
    private AutoTransition autoTransition;
    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
        //每过一秒钟监测一次wifi的网络状态
        mHandlerWifi.sendEmptyMessageDelayed(0, 1000);
    }

    //Wifi网络监测线程  然后在onCreate方法里面开启
    private Handler mHandlerWifi = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                //图标显示
                WifiStateUtils.checkWifiStateImg(SearchActivity.this, ivWifi);
                //文字显示
                tvWifi.setText(WifiStateUtils.checkWifiStateTxt(SearchActivity.this));
                sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    private void initView() {
        laySearch = (RelativeLayout) findViewById(R.id.lay_search);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        ivClose = (ImageView) findViewById(R.id.iv_close);
        edSearch = (EditText) findViewById(R.id.ed_search);

        WindowManager manager = getWindowManager();
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;  //获取屏幕的宽度 像素

        /**
         * 输入法键盘的搜索监听
         */
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String city = edSearch.getText().toString();
                    if (!TextUtils.isEmpty(city)) {
                        String md5 = Md5Utils.getMd5(city);
                        String MD5 = Md5Utils.getMD5(city);
                        Toast.makeText(SearchActivity.this, MD5, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SearchActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search://点击搜索 伸展
                initExpand();
                break;
            case R.id.iv_close://点击close 关闭
                initClose();
                break;
        }
    }

    /*设置伸展状态时的布局*/
    public void initExpand() {
        edSearch.setHint("输入城市名");
        edSearch.setHintTextColor(Color.WHITE);
        edSearch.setVisibility(View.VISIBLE);
        ivClose.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) laySearch.getLayoutParams();
        LayoutParams.width = dip2px(px2dip(width) - 40);
        LayoutParams.setMargins(dip2px(0), dip2px(0), dip2px(0), dip2px(0));
        laySearch.setPadding(14, 0, 14, 0);
        laySearch.setLayoutParams(LayoutParams);

        edSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edSearch.setFocusable(true);
                edSearch.setFocusableInTouchMode(true);
                return false;
            }
        });
        //开始动画
        beginDelayedTransition(laySearch);
    }

    /*设置收缩状态时的布局*/
    private void initClose() {
        edSearch.setVisibility(View.GONE);
        edSearch.setText("");
        ivClose.setVisibility(View.GONE);

        LinearLayout.LayoutParams LayoutParams = (LinearLayout.LayoutParams) laySearch.getLayoutParams();
        LayoutParams.width = dip2px(48);
        LayoutParams.height = dip2px(48);
        LayoutParams.setMargins(dip2px(0), dip2px(0), dip2px(0), dip2px(0));
        laySearch.setLayoutParams(LayoutParams);

        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getWindowToken(), 0);
        edSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edSearch.setCursorVisible(true);
            }
        });
        //开始动画
        beginDelayedTransition(laySearch);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void beginDelayedTransition(ViewGroup view) {
        autoTransition = new AutoTransition();
        autoTransition.setDuration(500);
        TransitionManager.beginDelayedTransition(view, autoTransition);
    }

    // dp 转成 px
    private int dip2px(float dpVale) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    // px 转成 dp
    private int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}