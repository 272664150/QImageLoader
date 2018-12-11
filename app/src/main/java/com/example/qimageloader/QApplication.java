package com.example.qimageloader;

import android.app.Application;

import com.android.vinci.Vinci;
import com.android.vinci.constant.LoaderStrategyEnum;

public class QApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Vinci.getInstance().init(LoaderStrategyEnum.GLIDE);
    }
}
