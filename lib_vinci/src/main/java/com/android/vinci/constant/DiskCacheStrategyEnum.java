package com.android.vinci.constant;

/**
 * 磁盘缓存策略
 */
public enum DiskCacheStrategyEnum {

    NONE, //表示不缓存任何内容

    DATA, //表示只缓存原始图片

    RESOURCE, //表示只缓存转换过后的图片

    ALL, //表示既缓存原始图片，也缓存转换过后的图片

    DEFAULT //表示根据图片资源智能地选择使用哪一种缓存策略
}
