package com.gy.mvvm_demo.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.databinding.ActivityHomeBinding;
import com.gy.mvvm_demo.databinding.DialogEditBinding;
import com.gy.mvvm_demo.databinding.DialogModifyUserInfoBinding;
import com.gy.mvvm_demo.databinding.NavHeaderBinding;
import com.gy.mvvm_demo.db.bean.User;
import com.gy.mvvm_demo.utils.CameraUtils;
import com.gy.mvvm_demo.utils.Constant;
import com.gy.mvvm_demo.utils.MVUtils;
import com.gy.mvvm_demo.utils.PermissionUtils;
import com.gy.mvvm_demo.utils.SizeUtils;
import com.gy.mvvm_demo.view.dialog.AlertDialog;
import com.gy.mvvm_demo.viewmodels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeActivity extends BaseActivity  {

    private ActivityHomeBinding binding;
    //可输入弹窗
    private AlertDialog editDialog = null;
    //修改用户信息弹窗
    private AlertDialog modifyUserInfoDialog = null;
    //是否显示修改头像的两种方式
    private boolean isShow = false;
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private String mCameraImagePath;
    private final String TAG = this.getClass().getSimpleName();
    private HomeViewModel homeViewModel;
    private User localUser;
    /**
     * 常规使用 通过意图进行跳转
     */
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    /**
     * 拍照活动结果启动器
     */
    private ActivityResultLauncher<Uri> takePictureActivityResultLauncher;
    /**
     * 相册活动结果启动器
     */
    private ActivityResultLauncher<String[]> openAlbumActivityResultLauncher;
    /**
     * 页面权限请求 结果启动器
     */
    private ActivityResultLauncher<String[]> permissionActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        register();
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        //显示加载弹框
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        showLoading();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        initView();
    }

    private void register() {
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                //从外部存储管理页面返回
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!isStorageManager()) {
                        showMsg("未打开外部存储管理开关，无法打开相册，抱歉");
                        return;
                    }
                }
                if (!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
//                    requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE);
                    permissionActivityResultLauncher.launch(new String[]{PermissionUtils.READ_EXTERNAL_STORAGE});
                    return;
                }
                //打开相册
                openAlbum();
            }
        });
        //调用MediaStore.ACTION_IMAGE_CAPTURE拍照，并将图片保存到给定的Uri地址，返回true表示保存成功。
        takePictureActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                modifyAvatar(mCameraUri.toString());
            }
        });
        // 提示用户选择文档，返回它的Uri。
        openAlbumActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), result -> {
            if (result != null){
                modifyAvatar(result.toString());
            }else{
                showMsg("返回对象为空，未选择本地照片");
            }
        });
        //多个权限返回结果
        permissionActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (result.containsKey(PermissionUtils.CAMERA)) {
                //相机权限
                if (!result.get(PermissionUtils.CAMERA)) {
                    showMsg("您拒绝了相机权限，无法打开相机，抱歉。");
                    return;
                }
                takePicture();
            } else if (result.containsKey(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                //文件读写权限
                if (!result.get(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                    showMsg("您拒绝了读写文件权限，无法打开相册，抱歉。");
                    return;
                }
                openAlbum();
            } else if (result.containsKey(PermissionUtils.LOCATION)) {
                //定位权限
                if (!result.get(PermissionUtils.LOCATION)) {
                    showMsg("您拒绝了位置许可，将无法使用地图，抱歉。");
                }
            }
        });
    }


    /**
     * 初始化控件
     */
    private void initView() {
        //获取navController
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        binding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.info_fragment:
                    binding.tvTitle.setText("热门资讯");
                    navController.navigate(R.id.info_fragment);
                    break;
                case R.id.map_fragment:
                    binding.tvTitle.setText("地图天气");
                    navController.navigate(R.id.map_fragment);
                    break;
                default:
            }
            return true;
        });
        binding.ivAvatar.setOnClickListener(v-> binding.drawerLayout.open());
        binding.navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.item_setting:

                    break;
                case R.id.item_logout:
                    logout();
                    break;
                case R.id.item_notebook:
                    jumpActivity(NotebookActivity.class);
                    break;
                default:
                    break;
            }
            return true;
        });

        //获取NavigationView的headerLayout视图
        View headerView = binding.navView.getHeaderView(0);
        ((View) headerView).setOnClickListener(v -> showModifyUserInfoDialog());
        //获取headerLayout视图的Binding
        NavHeaderBinding headerBinding = DataBindingUtil.bind(headerView);
        //获取本地用户信息
        homeViewModel.getUser();
        //用户信息发生改变时给对应的xml设置数据源也就是之前写好的ViewModel。
        homeViewModel.user.observe(this, user -> {
            localUser = user;
            binding.setHomeViewModel(homeViewModel);
            if (headerBinding != null) {
                headerBinding.setHomeViewModel(homeViewModel);
            }
            //隐藏加载弹窗
            dismissLoading();
        });
        //请求定位动态权限
        requestLocation();
    }

    /**
     * 请求定位权限
     */
    private void requestLocation() {
        if (isAndroid6()) {
            if (!hasPermission(PermissionUtils.LOCATION)) {
//                requestPermission(PermissionUtils.LOCATION);
                permissionActivityResultLauncher.launch(new String[]{PermissionUtils.LOCATION});
            }
        } else {
            showMsg("您无需动态请求权限");
        }
    }


    /**
     * 显示修改用户弹窗
     */
    private void showModifyUserInfoDialog() {
        DialogModifyUserInfoBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_modify_user_info, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px(this, 300), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_modify_avatar, v -> {
                    //修改头像，点击显示修改头像的方式，再次点击隐藏修改方式
                    binding.layModifyAvatar.setVisibility(isShow ? View.GONE : View.VISIBLE);
                    isShow = !isShow;
                }).setOnClickListener(R.id.tv_album_selection, v -> albumSelection())//相册选择
                .setOnClickListener(R.id.tv_camera_photo, v -> cameraPhoto())//相机拍照
                .setOnClickListener(R.id.tv_modify_nickname, v -> showEditDialog(0))//修改昵称
                .setOnClickListener(R.id.tv_modify_Introduction, v -> showEditDialog(1))//修改简介
                .setOnClickListener(R.id.tv_close, v -> modifyUserInfoDialog.dismiss())//关闭弹窗
                .setOnDismissListener(dialog -> isShow = false);
        modifyUserInfoDialog = builder.create();
        modifyUserInfoDialog.show();
    }

    /**
     * 相册选择
     */
    private void albumSelection() {
        modifyUserInfoDialog.dismiss();
        if (isAndroid11()) {
            //请求打开外部存储管理
            requestManageExternalStorage(intentActivityResultLauncher);
        } else {
            if (!isAndroid6()) {
                //打开相册
                openAlbum();
                return;
            }
            if (!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE);
                permissionActivityResultLauncher.launch(new String[]{PermissionUtils.READ_EXTERNAL_STORAGE});
                return;
            }
            //打开相册
            openAlbum();
        }
    }

    /**
     * 页面返回结果
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            showMsg("未知原因");
            return;
        }
        switch (requestCode) {
            case PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE:
                //从外部存储管理页面返回
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (!isStorageManager()) {
                        showMsg("未打开外部存储管理开关，无法打开相册，抱歉");
                        return;
                    }
                }
                if (!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                    requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE);
                    return;
                }
                //打开相册
                openAlbum();
                break;
            case SELECT_PHOTO_CODE:
                //相册中选择图片返回
                modifyAvatar(CameraUtils.getImageOnKitKatPath(data, this));
                break;
            case TAKE_PHOTO_CODE:
                //相机中拍照返回
                modifyAvatar(isAndroid10() ? mCameraUri.toString() : mCameraImagePath);
                break;
            default:
                break;
        }

    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        //startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO_CODE);
        openAlbumActivityResultLauncher.launch(new String[]{"image/*"});
    }

    /**
     * 修改头像
     */
    private void modifyAvatar(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            //保存到数据表中
            modifyContent(2, imagePath);
            Log.d(TAG, "modifyAvatar: " + imagePath);
        } else {
            showMsg("图片获取失败");
        }
    }

    /**
     * 修改内容
     *
     * @param type    类型 0：昵称 1：简介 2: 头像
     * @param content 修改内容
     */
    private void modifyContent(int type, String content) {
        if (type == 0) {
            localUser.setNickname(content);
        } else if (type == 1) {
            localUser.setIntroduction(content);
        } else if (type == 2) {
            localUser.setAvatar(content);
        }
        homeViewModel.updateUser(localUser);
        homeViewModel.failed.observe(this, failed -> {
            dismissLoading();
            if ("200".equals(failed)) {
                showMsg("修改成功");
            }
        });
    }

    /**
     * 相册拍照
     */
    private void cameraPhoto() {
        modifyUserInfoDialog.dismiss();
        if (!isAndroid6()) {
            //打开相机
            takePicture();
            return;
        }
        if (!hasPermission(PermissionUtils.CAMERA)) {
            permissionActivityResultLauncher.launch(new String[]{PermissionUtils.CAMERA});
//            requestPermission(PermissionUtils.CAMERA);
            return;
        }
        //打开相机
        takePicture();
    }

    /**
     * 新的拍照
     */
    private void takePicture() {
        mCameraUri = getContentResolver().insert(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        takePictureActivityResultLauncher.launch(mCameraUri);
    }

    /**
     * 显示可输入文字弹窗
     * @param type 0 修改昵称  1  修改简介
     */
    private void showEditDialog(int type) {
        modifyUserInfoDialog.dismiss();
        DialogEditBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_edit, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setCancelable(true)
                .setText(R.id.tv_title, type == 0 ? "修改昵称" : "修改简介")
                .setContentView(binding.getRoot())
                .setWidthAndHeight(SizeUtils.dp2px(this, 300), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_cancel, v -> editDialog.dismiss())
                .setOnClickListener(R.id.tv_sure, v -> {
                    String content = binding.etContent.getText().toString().trim();
                    if (content.isEmpty()) {
                        showMsg(type == 0 ? "请输入昵称" : "请输入简介");
                        return;
                    }
                    if (type == 0 && content.length() > 10) {
                        showMsg("昵称过长，请输入8个以内汉字或字母");
                        return;
                    }
                    editDialog.dismiss();
                    showLoading();
                    //保存输入的值
                    modifyContent(type, content);
                });
        editDialog = builder.create();
        binding.etContent.setHint(type == 0 ? "请输入昵称" : "请输入简介");
        editDialog.show();
    }

    /**
     * 退出登录
     */
    private void logout() {
        showMsg("退出登录");
        MVUtils.put(Constant.IS_LOGIN,false);
        jumpActivityFinish(LoginActivity.class);
    }

}