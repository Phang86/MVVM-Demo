package com.gy.mvvm_demo.ui.activity;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivityEditBinding;
import com.gy.mvvm_demo.db.bean.Notebook;
import com.gy.mvvm_demo.utils.EasyDate;
import com.gy.mvvm_demo.viewmodels.EditViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditActivity extends BaseActivity implements View.OnClickListener {

    private ActivityEditBinding binding;
    private InputMethodManager inputMethodManager;
    private final String TAG = "EditActivity";
    private HashMap<Integer, Integer> map;
    private EditViewModel viewModel;
    private int uid;
    private Notebook mNotebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit);
        viewModel = new ViewModelProvider(this).get(EditViewModel.class);
        //根据受否深色模式设置状态栏样式
        setStatusBar(!isNight());
        back(binding.toolbar);
        initView();
    }

    private void initView() {
        //监听输入
        listenInput(binding.etTitle);
        listenInput(binding.etContent);
        binding.ivOk.setOnClickListener(this);
        binding.ivDelete.setOnClickListener(this);

        uid = getIntent().getIntExtra("uid", -1);
        if (uid == -1) {
            showInput();
            showTime();
            binding.ivDelete.setVisibility(View.GONE);
        } else {
            //修改
            binding.ivDelete.setVisibility(View.VISIBLE);
            viewModel.queryById(uid);
            viewModel.notebook.observe(this, notebook -> {
                mNotebook = notebook;
                binding.setNotebook(mNotebook);
            });
            viewModel.failed.observe(this, result -> Log.e(TAG, result));
        }
    }

    /**
     * 监听输入
     *
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
                    if (binding.etTitle.getText().length() == 0 && binding.etContent.getText().length() == 0) {
                        binding.ivOk.setVisibility(View.GONE);
                    }
                }
                int wordsNumber = binding.etContent.getText().toString().length();
                binding.tvWordsNumber.setVisibility(wordsNumber > 0 ? View.VISIBLE : View.GONE);
                binding.tvWordsNumber.setText(wordsNumber + "字");
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
    private void showTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String year_month_day = sdf.format(new Date());
        String hour = EasyDate.getHour();
        String minute = EasyDate.getMinute();
        String str = "";
        if (Integer.parseInt(hour) > 12 && Integer.parseInt(hour) <= 17) {
            str = "下午";
        } else if (Integer.parseInt(hour) == 12) {
            str = "中午";
        } else if (Integer.parseInt(hour) >= 18) {
            str = "晚上";
        } else if (Integer.parseInt(hour) < 12 && Integer.parseInt(hour) >= 9) {
            str = "上午";
        } else if (Integer.parseInt(hour) < 9 && Integer.parseInt(hour) >= 7) {
            str = "早上";
        } else {
            str = "凌晨";
        }
        String timeNow = year_month_day + "  " + str + " " + hour + ":" + minute;
        //binding.tvTime.setText(timeNow);
        //mNotebook.setTime(timeNow);
        binding.setNotebook(new Notebook(timeNow, "", ""));
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
                if (uid == -1) {
                    viewModel.addNotebook(new Notebook(binding.tvTime.getText().toString(),
                            binding.etContent.getText().toString(),
                            binding.etTitle.getText().toString()));
                } else {
                    mNotebook.setTitle(binding.etTitle.getText().toString());
                    mNotebook.setContent(binding.etContent.getText().toString());
                    mNotebook.setTime(binding.tvTime.getText().toString());
                    viewModel.updateNotebook(mNotebook);
                }
                finish();
                break;
            case R.id.iv_delete:
                viewModel.deleteNotebook(mNotebook);
                finish();
                break;
            default:
                break;
        }
    }
}