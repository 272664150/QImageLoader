package com.android.vinci.transform;

/**
 * 图片加载库-圆角变换
 */
public final class RoundTransform implements ILoaderTransform {

    private float mRadius;

    public RoundTransform(float radius) {
        this.mRadius = radius;
    }

    public float getRadius() {
        return mRadius;
    }
}
