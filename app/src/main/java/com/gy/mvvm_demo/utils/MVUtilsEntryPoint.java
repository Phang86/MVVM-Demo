package com.gy.mvvm_demo.utils;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@EntryPoint
@InstallIn(ApplicationComponent.class)
public interface MVUtilsEntryPoint {
    MVUtils getMVUtils();
}

