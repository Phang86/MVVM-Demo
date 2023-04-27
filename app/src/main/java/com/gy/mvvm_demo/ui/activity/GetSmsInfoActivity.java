package com.gy.mvvm_demo.ui.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gy.mvvm_demo.R;
import com.gy.mvvm_demo.ui.adapter.SmsAdapter;
import com.gy.mvvm_demo.ui.bean.SmsInfo;
import com.gy.mvvm_demo.utils.SizeUtils;
import com.gy.mvvm_demo.view.dialog.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetSmsInfoActivity extends BaseActivity {

    private RecyclerView smsRv;
    private SmsAdapter smsAdapter;
    private List<SmsInfo> smsInfoList;
    private String[] SMS_PERMISSIONS = {Manifest.permission.READ_SMS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.READ_CONTACTS};
    private ActivityResultLauncher<String[]> permissionActivityResultLauncher;
    private long exitTime;
    private static AlertDialog privacyPolicyDialog;

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
    }

    @Override
    public void onBackPressed() {
        if (!smsInfoList.isEmpty() && smsInfoList.size() > 0) {
            smsInfoList.clear();
        }
        super.onBackPressed();
    }

    private void exitProgram(){
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(context, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
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


}