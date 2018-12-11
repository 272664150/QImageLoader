package com.android.vinci.transform;

/**
 * 图片加载库-上半部分圆角变换
 */
public final class TopHalfRoundTransform implements ILoaderTransform {

    private float mRadius;

    public TopHalfRoundTransform(float radius) {
        this.mRadius = radius;
    }

    public float getRadius() {
        return mRadius;
    }
}
