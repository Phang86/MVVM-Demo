package com.gy.mvvm_demo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.ui.bean.TestRvBean;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<TestRvBean> mList;

    public RecyclerAdapter(Context context, List<TestRvBean> list) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(R.layout.item_delete, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        BaseViewHolder viewHolder = (BaseViewHolder) holder;
        viewHolder.setText(R.id.item_tv_msg_title, mList.get(position).getTitle());
        viewHolder.setVisible(R.id.item_tv_msg_flag, mList.get(position).getFlag() % 2 == 0);
        viewHolder.setText(R.id.item_tv_test_msg_time, mList.get(position).getTime());
        viewHolder.setText(R.id.item_tv_test_msg_info, mList.get(position).getMsgInfo());
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }
}