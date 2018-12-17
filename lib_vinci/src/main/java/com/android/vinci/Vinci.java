package com.android.vinci;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintContextWrapper;
import android.view.View;

import com.android.vinci.config.LoaderOptions;
import com.android.vinci.constant.LoaderStrategyEnum;
import com.android.vinci.strategy.ILoaderStrategy;
import com.android.vinci.strategy.glide.GlideLoaderStrategy;

import java.io.File;

public class Vinci implements ILoaderStrategy {

    private ILoaderStrategy mLoaderStrategy;

    private Vinci() {
    }

    public static Vinci getInstance() {
        return SingletonHolder.mInstance;
    }

    private static class SingletonHolder {
        private static final Vinci mInstance = new Vinci();
    }

    public void init(@NonNull LoaderStrategyEnum loaderStrategy) {
        init(loaderStrategy, "");
    }

    public void init(@NonNull LoaderStrategyEnum loaderStrategy, @NonNull String cacheDirPath) {
        switch (loaderStrategy) {
            case GLIDE:
                mLoaderStrategy = new GlideLoaderStrategy(cacheDirPath);
                break;
            case OTHER:
                break;
        }
    }

    @Override
    public void load(@NonNull View v, @Nullable String url, @Nullable LoaderOptions options) {
        if (mLoaderStrategy == null || isInvalid(v)) {
            return;
        }
        mLoaderStrategy.load(v, url, options);
    }

    @Override
    public void load(@NonNull View v, @Nullable int resId, @Nullable LoaderOptions options) {
        if (mLoaderStrategy == null || isInvalid(v)) {
            return;
        }
        mLoaderStrategy.load(v, resId, options);
    }

    @Override
    public void load(@NonNull View v, @Nullable byte[] bytes, @Nullable LoaderOptions options) {
        if (mLoaderStrategy == null || isInvalid(v)) {
            return;
        }
        mLoaderStrategy.load(v, bytes, options);
    }

    @Override
    public void load(@NonNull View v, @Nullable Bitmap bitmap, @Nullable LoaderOptions options) {
        if (mLoaderStrategy == null || isInvalid(v)) {
            return;
        }
        mLoaderStrategy.load(v, bitmap, options);
    }

    @Override
    public <R> void load(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options, @Nullable ILoaderListener<R> listener) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.load(context, url, options, listener);
    }

    @Override
    public void preload(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.preload(context, url, options);
    }

    @Override
    public void downloadOnly(@NonNull Context context, @Nullable String url) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.downloadOnly(context, url);
    }

    @Override
    public <R> R submit(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options, @NonNull Class<R> clazz) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return null;
        }
        return mLoaderStrategy.submit(context, url, options, clazz);
    }

    @Override
    public void pause(@NonNull Context context) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.pause(context);
    }

    @Override
    public void resume(@NonNull Context context) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.resume(context);
    }

    @Override
    public void clear(@NonNull View v) {
        if (mLoaderStrategy == null || isInvalid(v)) {
            return;
        }
        mLoaderStrategy.clear(v);
    }

    @Override
    public void trimMemory(@NonNull Context context, @NonNull int level) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.trimMemory(context, level);
    }

    @Override
    public void cleanMemory(@NonNull Context context) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.cleanMemory(context);
    }

    @Override
    public void clearDiskCache(@NonNull Context context) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return;
        }
        mLoaderStrategy.clearDiskCache(context);
    }

    @Override
    public String getCacheDirPath(@NonNull Context context) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return null;
        }
        return mLoaderStrategy.getCacheDirPath(context);
    }

    @Override
    public File getCacheFile(@NonNull Context context, @NonNull String url) {
        if (mLoaderStrategy == null || isInvalid(context)) {
            return null;
        }
        return mLoaderStrategy.getCacheFile(context, url);
    }

    private boolean isInvalid(@NonNull View view) {
        return isInvalid(view != null ? view.getContext() : null);
    }

    private boolean isInvalid(@NonNull Context context) {
        if (context == null) {
            return true;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return activity.isFinishing() || activity.isDestroyed();
            } else {
                return activity.isFinishing();
            }
        } else if (context instanceof Service
                || context instanceof Application
                || context instanceof TintContextWrapper) {
            return false;
        }
        return true;
    }
}
