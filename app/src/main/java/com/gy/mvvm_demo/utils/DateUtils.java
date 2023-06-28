package com.gy.mvvm_demo.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.gy.mvvm_demo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static volatile DateUtils instance;

    public static DateUtils getInstance() {
        if (instance == null) {
            synchronized (DateUtils.class) {
                if (instance == null) {
                    instance = new DateUtils();
                }
            }
        }
        return instance;
    }

    public DateUtils() {
    }

    public interface ShowTimeSelectorCallback {
        void OnTimeSelectListener(Date date);
    }

    public void showDateSelector(Context context, View v, Calendar selectCalendar, final ShowTimeSelectorCallback callback) {
        showDateSelector(context, v, selectCalendar, "yyyyMMdd", callback);
    }

    public void showDateSelector(Context context, View v, Calendar selectCalendar, String format, final ShowTimeSelectorCallback callback) {
        WindowUtil.getInstance().hideSoftInput(v);
        TimePickerBuilder builder = new TimePickerBuilder(context, (date, v1) -> {
            if (callback != null) {
                callback.OnTimeSelectListener(date);
            }
        });
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(calendarEnd.get(Calendar.YEAR) + 10, 12, 31);
        TimePickerView timePickerView = builder
                .setType(new boolean[]{format.contains("yyyy"),
                        format.contains("MM"),
                        format.contains("dd"),
                        format.contains("HH"),
                        format.contains("mm"),
                        format.contains("ss")})
                .setLabel(
                        format.contains("yyyy") ? context.getString(R.string.year) : "",
                        format.contains("MM") ? context.getString(R.string.month) : "",
                        format.contains("dd") ? context.getString(R.string.day) : "",
                        format.contains("HH") ? context.getString(R.string.hours) : "",
                        format.contains("mm") ? context.getString(R.string.mins) : "",
                        format.contains("ss") ? context.getString(R.string.seconds) : "")
                .isCenterLabel(true).setDividerColor(Color.DKGRAY).setCancelText(context.getString(R.string.cancel))
                .setSubmitText(context.getString(R.string.confirm))
                .setLineSpacingMultiplier(3.0f)
                .setItemVisibleCount(7)
                .setDecorView(null)
                .setRangDate(startDate, calendarEnd)
                .build();
        timePickerView.setDate(selectCalendar);
        timePickerView.show();
    }

    public String DateTimeToStr(Date date) {
        return DateTimeToStr(date, "yyyy-MM-dd");
    }

    public String DateTimeToStr(Date date, String format) {
        //可根据需要自行截取数据显示
        if (date == null)
            return "";
        SimpleDateFormat simpleDateFormat = null;
        try {
            simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return simpleDateFormat.format(date);
    }

    public Date StrToDateTime(String str) {
        return StrToDateTime(str, "yyyy-MM-dd");
    }

    public Date StrToDateTime(String str, String format) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTimeFormatText(Date date) {
        // 一分鐘
        long minute = 60 * 1000;
        // 一小時
        long hour = 60 * minute;
        // 一天
        long day = 24 * hour;
        // 一個月
        long month = 31 * day;
        // 一年
        long year = 12 * month;
        if (date == null) {
            return null;
        }
        long diff = new Date().getTime() - date.getTime();
        long r;
        if (diff > year) {
            r = diff / year;
            return r + "年前";
        }
        if (diff > month) {
            r = diff / month;
            return r + "個月前";
        }
        if (diff > day) {
            r = diff / day;
            return r + "天前";
        }
        if (diff > hour) {
            r = diff / hour;
            return r + "小時前";
        }
        if (diff > minute) {
            r = diff / minute;
            return r + "分鐘前";
        }
        return "剛剛";
    }
}
