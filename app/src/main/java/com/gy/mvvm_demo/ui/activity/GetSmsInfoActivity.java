package com.gy.mvvm_demo.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.ui.adapter.SmsAdapter;
import com.gy.mvvm_demo.ui.bean.SmsInfo;
import com.gy.mvvm_demo.utils.SizeUtils;
import com.gy.mvvm_demo.view.dialog.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GetSmsInfoActivity extends BaseActivity {

    private RecyclerView smsRv;
    private SmsAdapter smsAdapter;
    private List<SmsInfo> smsInfoList;
    private String[] SMS_PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
    private String[] CAMERA_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 页面权限请求 结果启动器
     */
    private ActivityResultLauncher<String[]> permissionActivityResultLauncher;
    //
    private ActivityResultLauncher<String[]> getCameraPermissionActivityResultLauncher;
    private long exitTime;
    private static AlertDialog privacyPolicyDialog;
    /**
     * 外部存储权限请求码
     */
    public static final int REQUEST_EXTERNAL_STORAGE_CODE = 9527;
    /**
     * 打开相册请求码
     */
    private static final int OPEN_ALBUM_CODE = 100;
    /**
     * 图片剪裁请求码
     */
    public static final int PICTURE_CROPPING_CODE = 200;

    /**
     * Glide请求图片选项配置
     */
    private RequestOptions requestOptions = RequestOptions
            .circleCropTransform()//圆形剪裁
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_sms_info);
        initView();
    }

    private void initView() {
        checkPermission();
        smsInfoList = new ArrayList<>();
        smsRv = findViewById(R.id.sms_rv);
        smsRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @SuppressLint("Range")
    public String getPhoneSmsRecord() {
        String[] projection = new String[]{"_id", "address", "person", "body", "date", "type"};
        try {
            Uri smsUri = Uri.parse("content://sms/");
            Cursor cursor = getContentResolver().query(smsUri, projection, null, null, "date desc");
            String smsType;
            String smsName;
            String smsNumber;
            String smsBody;
            String smsDate;
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    smsType = cursor.getString(cursor.getColumnIndex("type"));
                    int type = Integer.parseInt(smsType);
                    if (type == 1) {
                        smsType = "接收";
                    } else if (type == 2) {
                        smsType = "发送";
                    } else {
                        smsType = "其他";
                    }
                    smsName = cursor.getString(cursor.getColumnIndex("person"));
                    if (smsName == null) {
                        smsName = "未知号码";
                    }

                    smsNumber = cursor.getString(cursor.getColumnIndex("address"));

                    smsName = getContactNameByAddress(smsNumber);
                    smsBody = cursor.getString(cursor.getColumnIndex("body"));
                    smsDate = cursor.getString(cursor.getColumnIndex("date"));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(Long.parseLong(smsDate));
                    smsDate = dateFormat.format(d);
                    Log.d("输出", smsType + smsName + smsNumber + smsBody + smsDate);
                    SmsInfo smsInfo = new SmsInfo(smsType, smsName, smsNumber, smsBody, smsDate);

                    smsInfoList.add(smsInfo);
                }
                smsAdapter = new SmsAdapter(smsInfoList);
                smsRv.setAdapter(smsAdapter);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getContactNameByAddress(String phoneNumber) {
        Uri personUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cur = context.getContentResolver().query(personUri,
                new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cur.moveToFirst()) {
            int nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            String name = cur.getString(nameIdx);
            cur.close();
            return name;
        }
        return "未知号码";
    }


    public void getPermission(View view) {
        showPrivacyPolicyDialog();
//        if (isAndroid6()) {
//            permissionActivityResultLauncher.launch(SMS_PERMISSIONS);
//        } else {
//            Toast.makeText(context, "无需动态权限申请", Toast.LENGTH_SHORT).show();
//        }
    }

    private void checkPermission() {
        permissionActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (result.containsKey(SMS_PERMISSIONS[0])) {
                if (!result.get(SMS_PERMISSIONS[0])) {
                    Toast.makeText(context, "请前往设置打开读取短信权限", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (result.containsKey(SMS_PERMISSIONS[1])) {
                if (!result.get(SMS_PERMISSIONS[1])) {
                    Toast.makeText(context, "请前往设置打开写短信权限", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (result.containsKey(SMS_PERMISSIONS[2])) {
                if (!result.get(SMS_PERMISSIONS[2])) {
                    Toast.makeText(context, "请前往设置打开读写联系人权限", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            getPhoneSmsRecord();
        });

        getCameraPermissionActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.OpenDocument(), result -> {
            if (result != null) {
                //返回图片路径
                Log.e("TAG", "getCameraPermissionActivityResultLauncher：" + result.toString());
            } else {
                showMsg("返回对象为空，未选择本地照片");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!smsInfoList.isEmpty() && smsInfoList.size() > 0) {
            smsInfoList.clear();
        }
        super.onBackPressed();
    }

    private void exitProgram() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            exitProgram();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 显示隐私政策入口弹窗
     */
    public void showPrivacyPolicyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .addDefaultAnimation()
                .setContentView(R.layout.dialog_privacy_policy)
                .setCancelable(false)
                .setWidthAndHeight(SizeUtils.dp2px(GetSmsInfoActivity.this, 280), LinearLayout.LayoutParams.WRAP_CONTENT)
                .setOnClickListener(R.id.tv_privacy_policy, v -> {
                    //startActivity(new Intent(this, PrivacyPolicyActivity.class));
                }).setOnClickListener(R.id.tv_no_used, v -> {
                    //ToastUtils.showShortToast(context,"不同意隐私政策，无法正常使用App，请退出App，重新进入。");
                    privacyPolicyDialog.dismiss();
                }).setOnClickListener(R.id.tv_agree, v -> {
                    //已同意隐私政策
                    //SPUtils.putBoolean(Constant.AGREE, true,context)
                    //友盟SDK初始化
                    //UMConfigure.init(this, Constant.U_MENG_APPKEY, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
                    //权限请求判断
                    //permissionVersion();
                    privacyPolicyDialog.dismiss();
                });
        privacyPolicyDialog = builder.create();
        privacyPolicyDialog.show();
    }

    public void openAlbum(View view) {
        //方法一：
//        getCameraPermissionActivityResultLauncher.launch(new String[]{"image/*"});

        //方法二：
        requestCameraPermissions();
    }

    @AfterPermissionGranted(REQUEST_EXTERNAL_STORAGE_CODE)
    private void requestCameraPermissions() {
        int i = 0;
        if (EasyPermissions.hasPermissions(this, CAMERA_PERMISSIONS)) {
            //已经获得权限
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, OPEN_ALBUM_CODE);
        } else {
            //无权限则进行请求
            EasyPermissions.requestPermissions(GetSmsInfoActivity.this, "请求权限", REQUEST_EXTERNAL_STORAGE_CODE,CAMERA_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发给 EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 图片剪裁
     *
     * @param uri 图片uri
     */
    private void pictureCropping(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        // 返回裁剪后的数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PICTURE_CROPPING_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_ALBUM_CODE && resultCode == RESULT_OK) {
            //得到选择后的图片地址
            Uri uri = Objects.requireNonNull(data).getData();
            pictureCropping(uri);
        } else if (requestCode == PICTURE_CROPPING_CODE && resultCode == RESULT_OK) {
            //图片剪裁返回
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                //在这里获得了剪裁后的Bitmap对象，可以用于上传
                Bitmap image = bundle.getParcelable("data");
                //设置到ImageView上
//                ivPicture.setImageBitmap(image);
                ImageView ivPicture = findViewById(R.id.iv_picture);
//                imageView.setImageBitmap(image);
//                Log.e("TAG", "onActivityResult: "+image.toString());

                Glide.with(this).load(image).apply(requestOptions).into(ivPicture);
            }
        }
    }
}