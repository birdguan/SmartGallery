package com.birdguan.smartgallery.impl;

import com.birdguan.smartgallery.base.IImageUriFetch;

import java.util.List;

/**
 * @Author: birdguan
 * @Date: 2020/6/4 15:24
 */
public class LocalFrameImageUriFetch implements IImageUriFetch {
    private static final String TAG = "SmartGallery: LocalFrameImageUriFetch";

    private static LocalFrameImageUriFetch mLocalFrameImageUriFetch;

    public static LocalFrameImageUriFetch getInstance() {
        if (mLocalFrameImageUriFetch == null) {
            synchronized (SystemImageUriFetch.class) {
                if (mLocalFrameImageUriFetch == null) {
                    mLocalFrameImageUriFetch = new LocalFrameImageUriFetch();
                }
            }
        }
        return mLocalFrameImageUriFetch;
    }

    private LocalFrameImageUriFetch() {
    }

    @Override
    public List<String> getAllImageUriList() {
        return null;
    }

    @Override
    public List<String> getRangeImageUriList(int start, int end) {
        return null;
    }

    @Override
    public List<String> getAllImageUriListFromTag(Object tag) {
        return null;
    }

    @Override
    public List<Object> getAllTag() {
        return null;
    }

    @Override
    public void freshImageInfo() {

    }
}
