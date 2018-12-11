package com.android.vinci.config;

import android.graphics.Bitmap;

import com.android.vinci.constant.DiskCacheStrategyEnum;
import com.android.vinci.constant.ImageTypeEnum;
import com.android.vinci.transform.ILoaderTransform;

import java.util.Arrays;
import java.util.List;

/**
 * 图片加载库-配置项
 */
public final class LoaderOptions {

    private int mHolderDrawable;   //占位图id
    private int mErrorDrawable;    //请求失败图片id
    private int mFallbackDrawable; //请求的url/model为null图片id

    private ImageSize mImageSize;        //设置图片大小
    private ImageTypeEnum mImageType;    //图片展示类型
    private Bitmap.Config mBitmapConfig; //图片色彩模式
    private List<ILoaderTransform> mTransforms; //图片变换模式

    private boolean isSkipMemoryCache;                //是否跳过内存缓存
    private DiskCacheStrategyEnum mDiskCacheStrategy; //磁盘缓存策略

    private LoaderOptions() {
    }

    public static class Builder {
        private LoaderOptions mOptions;

        public Builder() {
            mOptions = new LoaderOptions();
            mOptions.setHolderDrawable(-1);
            mOptions.setErrorDrawable(-1);
            mOptions.setFallbackDrawable(-1);
        }

        public Builder setHolderDrawable(int holderDrawable) {
            mOptions.setHolderDrawable(holderDrawable);
            return this;
        }

        public Builder setErrorDrawable(int errorDrawable) {
            mOptions.setErrorDrawable(errorDrawable);
            return this;
        }

        public Builder setFallbackDrawable(int fallbackDrawable) {
            mOptions.setFallbackDrawable(fallbackDrawable);
            return this;
        }

        public Builder setImageSize(int width, int height) {
            mOptions.setImageSize(new ImageSize(width, height));
            return this;
        }

        public Builder asGif() {
            mOptions.setImageType(ImageTypeEnum.AS_GIF);
            return this;
        }

        public Builder asFile() {
            mOptions.setImageType(ImageTypeEnum.AS_FILE);
            return this;
        }

        public Builder asBitmap() {
            mOptions.setImageType(ImageTypeEnum.AS_BITMAP);
            return this;
        }

        public Builder asDrawable() {
            mOptions.setImageType(ImageTypeEnum.AS_DRAWABLE);
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config bitmapConfig) {
            mOptions.setBitmapConfig(bitmapConfig);
            return this;
        }

        public Builder setTransforms(ILoaderTransform... transforms) {
            mOptions.setTransforms(transforms);
            return this;
        }

        public Builder setSkipMemoryCache(boolean isSkipMemoryCache) {
            mOptions.setSkipMemoryCache(isSkipMemoryCache);
            return this;
        }

        public Builder setDiskCacheStrategy(DiskCacheStrategyEnum diskCacheStrategy) {
            mOptions.setDiskCacheStrategy(diskCacheStrategy);
            return this;
        }

        public LoaderOptions build() {
            return mOptions;
        }
    }

    private void setHolderDrawable(int holderDrawable) {
        this.mHolderDrawable = holderDrawable;
    }

    private void setErrorDrawable(int errorDrawable) {
        this.mErrorDrawable = errorDrawable;
    }

    private void setFallbackDrawable(int fallbackDrawable) {
        this.mFallbackDrawable = fallbackDrawable;
    }

    private void setImageSize(ImageSize imageSize) {
        this.mImageSize = imageSize;
    }

    private void setImageType(ImageTypeEnum imageType) {
        this.mImageType = imageType;
    }

    private void setBitmapConfig(Bitmap.Config bitmapConfig) {
        this.mBitmapConfig = bitmapConfig;
    }

    private void setTransforms(ILoaderTransform... transforms) {
        if (transforms == null) {
            return;
        }
        this.mTransforms = Arrays.asList(transforms);
    }

    private void setSkipMemoryCache(boolean isSkipMemoryCache) {
        this.isSkipMemoryCache = isSkipMemoryCache;
    }

    private void setDiskCacheStrategy(DiskCacheStrategyEnum diskCacheStrategy) {
        this.mDiskCacheStrategy = diskCacheStrategy;
    }

    public int getHolderDrawable() {
        return mHolderDrawable;
    }

    public int getErrorDrawable() {
        return mErrorDrawable;
    }

    public int getFallbackDrawable() {
        return mFallbackDrawable;
    }

    public ImageSize getImageSize() {
        return mImageSize;
    }

    public ImageTypeEnum getImageType() {
        return mImageType;
    }

    public Bitmap.Config getBitmapConfig() {
        return mBitmapConfig;
    }

    public List<ILoaderTransform> getTransforms() {
        return mTransforms;
    }

    public boolean isSkipMemoryCache() {
        return isSkipMemoryCache;
    }

    public DiskCacheStrategyEnum getDiskCacheStrategy() {
        return mDiskCacheStrategy;
    }

    public static class ImageSize {
        private int mWidth;
        private int mHeight;

        public ImageSize(int width, int height) {
            this.mWidth = width;
            this.mHeight = height;
        }

        public int getWidth() {
            return mWidth;
        }

        public int getHeight() {
            return mHeight;
        }
    }
}
