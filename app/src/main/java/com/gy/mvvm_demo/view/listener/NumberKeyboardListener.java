package com.gy.mvvm_demo.view.listener;

public interface NumberKeyboardListener {
    /**
     * 数字字符
     * @param num 0~9
     */
    void onNum(String num);

    /**
     * 删除
     */
    void onDelete();

    /**
     * 完成
     */
    void onComplete();

    /**
     * 弹窗关闭
     */
    void onDialogDismiss();

    /**
     * 弹窗显示
     */
    void onDialogShow();
}

