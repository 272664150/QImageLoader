package com.android.vinci.config.glide;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.android.vinci.config.LoaderOptions;
import com.android.vinci.constant.DiskCacheStrategyEnum;
import com.android.vinci.transform.CenterCropTransform;
import com.android.vinci.transform.ILoaderTransform;
import com.android.vinci.transform.RoundTransform;
import com.android.vinci.transform.TopHalfRoundTransform;
import com.android.vinci.transform.glide.GlideTopHalfRoundTransform;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片加载库-处理Glide配置项
 */
public final class GlideOptionsProcessor {

    private GlideOptionsProcessor() {
    }

    public static RequestOptions convert(@NonNull LoaderOptions options) {
        RequestOptions requestOptions = new RequestOptions();
        if (options == null) {
            return requestOptions;
        }

        if (options.getHolderDrawable() != -1) {
            requestOptions.placeholder(options.getHolderDrawable());
        }

        if (options.getErrorDrawable() != -1) {
            requestOptions.error(options.getErrorDrawable());
        }

        if (options.getFallbackDrawable() != -1) {
            requestOptions.fallback(options.getFallbackDrawable());
        }

        if (options.getImageSize() != null) {
            requestOptions.override(options.getImageSize().getWidth(), options.getImageSize().getHeight());
        }

        if (options.getBitmapConfig() != null) {
            requestOptions.format(toDecodeFormat(options.getBitmapConfig()));
        }

        if (options.getTransforms() != null) {
            List<Transformation<Bitmap>> transforms = new ArrayList<>();
            for (ILoaderTransform transform : options.getTransforms()) {
                if (transform instanceof RoundTransform) {
                    transforms.add(new RoundedCorners((int) ((RoundTransform) transform).getRadius()));
                } else if (transform instanceof TopHalfRoundTransform) {
                    transforms.add(new GlideTopHalfRoundTransform(((TopHalfRoundTransform) transform).getRadius()));
                } else if (transform instanceof CenterCropTransform) {
                    transforms.add(new CenterCrop());
                }
            }
            requestOptions.transforms(new MultiTransformation(transforms));
        }

        if (options.isSkipMemoryCache()) {
            requestOptions.skipMemoryCache(true);
        }

        if (options.getDiskCacheStrategy() != DiskCacheStrategyEnum.DEFAULT) {
            if (options.getDiskCacheStrategy() == DiskCacheStrategyEnum.NONE) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            } else if (options.getDiskCacheStrategy() == DiskCacheStrategyEnum.DATA) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
            } else if (options.getDiskCacheStrategy() == DiskCacheStrategyEnum.RESOURCE) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
            } else if (options.getDiskCacheStrategy() == DiskCacheStrategyEnum.ALL) {
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
            }
        }

        return requestOptions;
    }

    private static DecodeFormat toDecodeFormat(Bitmap.Config config) {
        DecodeFormat df = DecodeFormat.DEFAULT;
        if (config == Bitmap.Config.ARGB_8888) {
            df = DecodeFormat.PREFER_ARGB_8888;
        } else if (config == Bitmap.Config.RGB_565) {
            df = DecodeFormat.PREFER_RGB_565;
        }
        return df;
    }
}
