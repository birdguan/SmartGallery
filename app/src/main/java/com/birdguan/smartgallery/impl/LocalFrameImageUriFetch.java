package com.birdguan.smartgallery.impl;

import android.net.Uri;

import com.birdguan.smartgallery.base.IImageUriFetch;

import java.io.File;
import java.util.Arrays;
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

        return Arrays.asList(Uri.fromFile(new File("/storage/emulated/0/voicenote/user/90ca451227212a9b/image/icon.jpg")).toString() ,
                Uri.fromFile(new File("/storage/emulated/0/Pictures/Photoshop Express/PSX_20180304_150049.jpg")).toString() ,
                Uri.fromFile(new File("/storage/emulated/0/UTDFiles/.c2868553337003ca80db0d102d639f74.png")).toString());

    }

    @Override
    public List<String> getRangeImageUriList(int start, int end) {
        throw new RuntimeException("没有实现");
    }

    @Override
    public List<String> getAllImageUriListFromTag(Object tag) {
        throw new RuntimeException("没有实现");
    }

    @Override
    public List<String> getRangeImageUriListFromTag(Object tag , int start , int end) {
        throw new RuntimeException("没有实现");
    }

    @Override
    public List<Object> getAllTag() {
        throw new RuntimeException("没有实现");
    }

    @Override
    public void freshImageInfo() {
        throw new RuntimeException("没有实现");
    }
}
