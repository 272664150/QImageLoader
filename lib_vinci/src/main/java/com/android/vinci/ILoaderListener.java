package com.android.vinci;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 图片加载库-回调Listener
 */
public interface ILoaderListener<R> {

    void onLoadStarted(@Nullable Drawable placeholder);

    void onResourceReady(@NonNull R resource);

    void onLoadFailed(@Nullable Drawable errorDrawable);

    void onLoadCleared(@Nullable Drawable placeholder);
}
