package com.gy.mvvm_demo.ui.activity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivityEditBinding;
import com.gy.mvvm_demo.utils.EasyDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class EditActivity extends BaseActivity implements View.OnClickListener {

    private ActivityEditBinding binding;
    private InputMethodManager inputMethodManager;
    private final String TAG = "EditActivity";
    private HashMap<Integer,Integer> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        initView();
    }

    private void initView() {
        setStatusBar(true);
        back(binding.toolbar);
        //监听输入
        listenInput(binding.etTitle);
        listenInput(binding.etContent);
        binding.ivOk.setOnClickListener(this);
        showInput();
    }

    /**
     * 监听输入
     * @param editText 输入框
     */
    private void listenInput(final AppCompatEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    binding.ivOk.setVisibility(View.VISIBLE);
                } else {
                    if (binding.etTitle.getText().length() == 0 && binding.etContent.getText().length() == 0  ){
                        binding.ivOk.setVisibility(View.GONE);
                    }
                }
                int wordsNumber = binding.etContent.getText().toString().length();
                binding.tvWordsNumber.setVisibility(wordsNumber > 0 ? View.VISIBLE : View.GONE);
                binding.tvWordsNumber.setText(wordsNumber+"字");
            }
        });
    }

    /**
     * 显示键盘
     */
    public void showInput() {
        binding.etContent.requestFocus();
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 显示当前时间
     */
    private void showTime(){
//        EasyDate.get
    }



    /**
     * 隐藏键盘
     */
    public void dismiss() {
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(binding.etContent.getWindowToken(), 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_ok:
                showMsg("提交");
                break;
            default:
                break;
        }
    }
}