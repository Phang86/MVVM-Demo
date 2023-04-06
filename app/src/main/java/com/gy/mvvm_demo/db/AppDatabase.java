package com.gy.mvvm_demo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.gy.mvvm_demo.db.bean.Image;
import com.gy.mvvm_demo.db.dao.ImageDao;

@Database(entities = {Image.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ImageDao imageDao();
}

