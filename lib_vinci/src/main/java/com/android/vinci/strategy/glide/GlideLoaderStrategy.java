package com.android.vinci.strategy.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.android.vinci.ILoaderListener;
import com.android.vinci.config.LoaderOptions;
import com.android.vinci.config.glide.GlideOptionsProcessor;
import com.android.vinci.constant.ImageTypeEnum;
import com.android.vinci.strategy.ILoaderStrategy;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

/**
 * Glide加载策略
 */
public final class GlideLoaderStrategy implements ILoaderStrategy {

    private String mCacheDirPath;

    public GlideLoaderStrategy(String cacheDirPath) {
        mCacheDirPath = cacheDirPath;
    }

    @Override
    public void load(@NonNull View v, @Nullable String url, @Nullable LoaderOptions options) {
        if (v instanceof ImageView) {
            getRequestBuilder(v.getContext(), options)
                    .load(url)
                    .into((ImageView) v);
        }
    }

    @Override
    public void load(@NonNull View v, @Nullable int resId, @Nullable LoaderOptions options) {
        if (v instanceof ImageView) {
            getRequestBuilder(v.getContext(), options)
                    .load(resId)
                    .into((ImageView) v);
        }
    }

    @Override
    public void load(@NonNull View v, @Nullable byte[] bytes, @Nullable LoaderOptions options) {
        if (v instanceof ImageView) {
            getRequestBuilder(v.getContext(), options)
                    .load(bytes)
                    .into((ImageView) v);
        }
    }

    @Override
    public void load(@NonNull View v, @Nullable Bitmap bitmap, @Nullable LoaderOptions options) {
        if (v instanceof ImageView) {
            getRequestBuilder(v.getContext(), options)
                    .load(bitmap)
                    .into((ImageView) v);
        }
    }

    @Override
    public <R> void load(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options, @Nullable final ILoaderListener<R> listener) {
        ImageTypeEnum imageType = ImageTypeEnum.DEFAULT;
        Type type = listener.getClass().getGenericInterfaces()[0];
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type[] types = pType.getActualTypeArguments();
            if (types != null && types.length > 0) {
                if (types[0].equals(Bitmap.class)) {
                    imageType = ImageTypeEnum.AS_BITMAP;
                } else if (types[0].equals(Drawable.class)) {
                    imageType = ImageTypeEnum.AS_DRAWABLE;
                }
            }
        }

        getRequestBuilder(context, options, imageType)
                .load(url)
                .into(new Target<R>() {
                    private Request mRequest;

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        listener.onLoadStarted(placeholder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        listener.onLoadFailed(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull R resource, @Nullable Transition<? super R> transition) {
                        listener.onResourceReady(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        listener.onLoadCleared(placeholder);
                    }

                    @Override
                    public void getSize(@NonNull SizeReadyCallback cb) {
                        cb.onSizeReady(Integer.MIN_VALUE, Integer.MIN_VALUE);
                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {
                    }

                    @Override
                    public void setRequest(@Nullable Request request) {
                        this.mRequest = request;
                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return mRequest;
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onStop() {
                    }

                    @Override
                    public void onDestroy() {
                    }
                });
    }

    @Override
    public void preload(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options) {
        getRequestBuilder(context, options)
                .load(url)
                .preload();
    }

    @Override
    public void downloadOnly(@NonNull Context context, @Nullable String url) {
        // 只缓存原始图片到磁盘
        getRequestManager(context)
                .downloadOnly()
                .load(url)
                .submit();
    }

    @Override
    public <R> R submit(@NonNull Context context, @Nullable String url, @Nullable LoaderOptions options, @NonNull Class<R> clazz) {
        // 目前支持 Bitmap、Drawable 2种格式
        ImageTypeEnum imageType;
        if (clazz.equals(Bitmap.class)) {
            imageType = ImageTypeEnum.AS_BITMAP;
        } else if (clazz.equals(Drawable.class)) {
            imageType = ImageTypeEnum.AS_DRAWABLE;
        } else {
            imageType = ImageTypeEnum.DEFAULT;
        }

        FutureTarget<R> futureTarget = getRequestBuilder(context, options, imageType)
                .load(url)
                .submit();

        try {
            return futureTarget.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void pause(@NonNull Context context) {
        getRequestManager(context)
                .pauseRequests();
    }

    @Override
    public void resume(@NonNull Context context) {
        getRequestManager(context)
                .resumeRequests();
    }

    @Override
    public void clear(@NonNull View v) {
        if (v instanceof ImageView) {
            getRequestManager(v.getContext())
                    .clear(v);
        }
    }

    @Override
    public void trimMemory(@NonNull Context context, @NonNull int level) {
        Glide.get(context)
                .trimMemory(level);
    }

    @Override
    public void cleanMemory(@NonNull Context context) {
        // 必须在UI线程中调用
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Glide.get(context)
                    .clearMemory();
        }
    }

    @Override
    public void clearDiskCache(@NonNull Context context) {
        // 必须在后台线程中调用
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Glide.get(context)
                    .clearDiskCache();
        }
    }

    @Override
    public String getCacheDirPath(@NonNull Context context) {
        if (TextUtils.isEmpty(mCacheDirPath)) {
            return Glide.getPhotoCacheDir(context).getAbsolutePath();
        } else {
            return mCacheDirPath + "glide" + File.separator;
        }
    }

    @Override
    public File getCacheFile(@NonNull Context context, @NonNull String url) {
        try {
            DiskCache diskCache = DiskLruCacheWrapper.create(new File(getCacheDirPath(context)), Integer.MAX_VALUE);
            GlideUrl glideUrl = new GlideUrl(url);
            File cacheFile = diskCache.get(glideUrl);
            return cacheFile;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private RequestBuilder getRequestBuilder(Context context, LoaderOptions options) {
        return getRequestBuilder(context, options, ImageTypeEnum.DEFAULT);
    }

    private RequestBuilder getRequestBuilder(Context context, LoaderOptions options, ImageTypeEnum imageType) {
        if (options != null &&
                options.getImageType() != null &&
                imageType == ImageTypeEnum.DEFAULT) {
            imageType = options.getImageType();
        }

        RequestBuilder builder;
        switch (imageType) {
            case AS_GIF:
                builder = getRequestManager(context).asGif();
                break;
            case AS_FILE:
                builder = getRequestManager(context).asFile();
                break;
            case AS_BITMAP:
                builder = getRequestManager(context).asBitmap();
                break;
            case AS_DRAWABLE:
                builder = getRequestManager(context).asDrawable();
                break;
            case DEFAULT:
            default:
                builder = getRequestManager(context).asDrawable();
                break;
        }
        builder.apply(GlideOptionsProcessor.convert(options));

        return builder;
    }

    private RequestManager getRequestManager(Context context) {
        return Glide.with(context);
    }
}
