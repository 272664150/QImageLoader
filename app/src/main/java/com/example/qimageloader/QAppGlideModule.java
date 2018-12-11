package com.example.qimageloader;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.vinci.Vinci;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public class QAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

        //新版的默认格式：ARGB_8888
        builder.setDefaultRequestOptions(
                new RequestOptions()
                        .format(DecodeFormat.PREFER_RGB_565)
        );

        //磁盘缓存的路径、大小
        builder.setDiskCache(
                new DiskLruCacheFactory(Vinci.getInstance().getCacheDirPath(context), 100 * 1024 * 1024)
        );

        super.applyOptions(context, builder);
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}