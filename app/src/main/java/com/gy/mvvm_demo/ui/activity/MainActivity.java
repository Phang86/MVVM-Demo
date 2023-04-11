package com.gy.mvvm_demo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.adapter.WallPaperAdapter;
import com.gy.mvvm_demo.databinding.ActivityMainBinding;
import com.gy.mvvm_demo.viewmodels.MainViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity {

    private ActivityMainBinding dataBinding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //数据绑定视图
        dataBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        //网络请求
        mainViewModel.getBiying();
        //返回数据时更新ViewModel，ViewModel更新则xml更新
        mainViewModel.biying.observe(this,biYingResponse -> dataBinding.setViewModel(mainViewModel));

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        dataBinding.rv.setLayoutManager(manager);

        //热门壁纸 网络请求
        mainViewModel.getWallPaper();
        mainViewModel.wallPaper.observe(this, wallPaperResponse -> dataBinding.rv.setAdapter(new WallPaperAdapter(wallPaperResponse.getRes().getVertical())));
        //伸缩偏移量监听
        dataBinding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {//收缩时
                    dataBinding.toolbarLayout.setTitle("MVVM-Demo");
                    isShow = true;
                } else if (isShow) {//展开时
                    dataBinding.toolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        });
        //页面上下滑动监听
        dataBinding.scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY > oldScrollY) {
                //上滑
                dataBinding.fabHome.hide();
            } else {
                //下滑
                dataBinding.fabHome.show();
            }
        });

    }

    public void toHome(View view) {
        jumpActivity(HomeActivity.class);
    }

}