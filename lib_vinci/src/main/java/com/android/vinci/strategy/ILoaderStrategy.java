package com.android.vinci.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.view.View;

import com.android.vinci.ILoaderListener;
import com.android.vinci.config.LoaderOptions;

import java.io.File;

/**
 * 图片加载库-策略接口
 */
public interface ILoaderStrategy {

    void load(@NonNull View v, @Nullable String url, @Nullable LoaderOptions options);

    void load(@NonNull View v, @RawRes @DrawableRes @Nullable int resId, @Nullable LoaderOptions options);

    void load(@NonNull View v, @Nullable byte[] bytes, @Nullable LoaderOptions options);

    void load(@NonNull View v, @Nullable Bitmap bitmap, @Nullable LoaderOptions options);

    <R> void load(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options, @Nullable ILoaderListener<R> listener);

    void preload(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options);

    void downloadOnly(@NonNull Context context, @Nullable String url);

    <R> R submit(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options, @NonNull Class<R> clazz);

    void pause(@NonNull Context context);

    void resume(@NonNull Context context);

    void clear(@NonNull View v);

    void trimMemory(@NonNull Context context, @NonNull int level);

    void cleanMemory(@NonNull Context context);

    void clearDiskCache(@NonNull Context context);

    String getCacheDirPath(@NonNull Context context);

    File getCacheFile(@NonNull Context context, @NonNull String url);
}
