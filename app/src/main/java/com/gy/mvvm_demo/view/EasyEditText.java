package com.gy.mvvm_demo.view;

import android.content.Context;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gy.mvvm_demo.view.listener.NumberKeyboardListener;

public class EasyEditText extends View  implements NumberKeyboardListener {


    public EasyEditText(Context context) {
        super(context);
    }

    public EasyEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EasyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNum(String num) {

    }

    @Override
    public void onDelete() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onDialogDismiss() {

    }

    @Override
    public void onDialogShow() {

    }

}
