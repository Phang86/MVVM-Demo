package com.gy.mvvm_demo.ui.adapter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.ui.bean.SmsInfo;

import java.util.List;

public class SmsAdapter extends BaseQuickAdapter<SmsInfo, BaseViewHolder> {

    public SmsAdapter(List<SmsInfo> data) {
        super(R.layout.item_smsinfo,data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, SmsInfo smsInfos) {
        baseViewHolder.setText(R.id.item_sms_type,smsInfos.getType())
                .setText(R.id.item_sms_name,smsInfos.getName())
                .setText(R.id.item_sms_body,smsInfos.getBody())
                .setText(R.id.item_sms_data,smsInfos.getDate())
                .setText(R.id.item_sms_number,smsInfos.getNumber());
    }
}
