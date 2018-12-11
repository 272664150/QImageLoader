package com.android.vinci.transform.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Glide上半部分圆角转换
 */
public final class GlideTopHalfRoundTransform extends BitmapTransformation {

    private float mRadius;

    public GlideTopHalfRoundTransform(float radius) {
        this.mRadius = radius;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) {
            return null;
        }

        try {
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), getAlphaSafeConfig(source));
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), getAlphaSafeConfig(source));
            }

            Canvas canvas = new Canvas(result);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            Path path = new Path();
            path.addRoundRect(rectF, new float[]{mRadius, mRadius, mRadius, mRadius, 0, 0, 0, 0}, Path.Direction.CCW);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            canvas.drawPath(path, paint);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(getClass().getName().getBytes(CHARSET));
    }

    private Bitmap.Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16.equals(inBitmap.getConfig())) { // NOPMD
                return Bitmap.Config.RGBA_F16;
            }
        }
        return Bitmap.Config.ARGB_8888;
    }
}
