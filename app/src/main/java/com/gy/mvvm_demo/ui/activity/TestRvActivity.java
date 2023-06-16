package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivityTestRvBinding;
import com.gy.mvvm_demo.ui.adapter.OnItemClickListener;
import com.gy.mvvm_demo.ui.adapter.RecyclerAdapter;
import com.gy.mvvm_demo.ui.bean.TestRvBean;
import com.gy.mvvm_demo.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class TestRvActivity extends AppCompatActivity{

    private ActivityTestRvBinding binding;
    private List<TestRvBean> mList = new ArrayList<>();
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestRvBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new RecyclerAdapter(this, mList);
        for (int i = 0; i < 50; i++) {
            if (i % 2 == 0) {
                mList.add(new TestRvBean(i, DateUtil.getNowTime(), "小明同來了哈哈哈哈啊啊哈哈小明同來了哈哈哈哈啊啊哈哈系統通知一條消息"));
            } else {
                mList.add(new TestRvBean(i, DateUtil.getNowTime(), "系統通知一條消息系統通知一條消息系統通知一條消息系統通知一條消息"));
            }
        }
        binding.testRv.setLayoutManager(new LinearLayoutManager(this));
        binding.testRv.setItemAnimator(new DefaultItemAnimator());
        binding.testRv.setAdapter(adapter);

        binding.testRv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                TestRvBean bean = mList.get(position);
                //判断此条消息是否被用户阅读过
                if (bean.getFlag() % 2 == 0) {
                    //如果未阅读，则取消未阅读标记
                    bean.setFlag(1);
                    //刷新当前数据源
                    adapter.notifyDataSetChanged();
                }

                Intent intent = new Intent(TestRvActivity.this, MsgInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("msg_info",bean);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                Log.e("TAG", "onDeleteClick: " + position);
                adapter.removeItem(position);
            }
        });
    }

}