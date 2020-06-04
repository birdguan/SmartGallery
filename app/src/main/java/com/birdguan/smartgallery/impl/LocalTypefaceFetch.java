package com.birdguan.smartgallery.impl;

import android.graphics.Typeface;

import com.birdguan.smartgallery.SmartGalleryApplication;
import com.birdguan.smartgallery.base.ITypefaceFetch;
import com.birdguan.smartgallery.base.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: birdguan
 * @Date: 2020/6/4 16:41
 */
public class LocalTypefaceFetch implements ITypefaceFetch {
    private static final String TAG = "SmartGallery: LocalTypefaceFetch";

    private static LocalTypefaceFetch mLocalTypefaceFetch;
    private static final Map<String, Typeface> mTypeface = new HashMap<>();

    private static final List<String> mTypefaceNames = new ArrayList<>();
    static {
        mTypefaceNames.add("光棍体");
        mTypefaceNames.add("甜妞体");
    }

    public static LocalTypefaceFetch getInstance() {
        if (mLocalTypefaceFetch == null) {
            synchronized (SystemImageUriFetch.class) {
                if (mLocalTypefaceFetch == null) {
                    mLocalTypefaceFetch = new LocalTypefaceFetch();
                    ITypefaceFetch.addITypefaceFetch(mLocalTypefaceFetch);
                }
            }
        }
        return mLocalTypefaceFetch;
    }
    @Override
    public List<Typeface> getAllTypeface() {
        if (mTypefaceNames.size() != mTypeface.size()) {
            for (String typefaceName : mTypefaceNames) {
                getTypeface(typefaceName);
            }
        }
        MyLog.d(TAG, "getAllTypeface", "状态:mTypefaceNames:mTypeface:",
                "", mTypefaceNames, mTypeface);
        return new ArrayList<>(mTypeface.values());
    }

    @Override
    public List<String> getAllTypefaceName() {
        return mTypefaceNames;
    }

    @Override
    public Typeface getTypeface(String typefaceName) {
        MyLog.d(TAG, "getTypeface", "状态:typefaceName:",
                "", typefaceName);
        Typeface typeface = mTypeface.get(typefaceName);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(SmartGalleryApplication.getAppContext().getAssets(),
                    "fonts/" + typefaceName + ".ttf");
            mTypeface.put(typefaceName, typeface);
        }
        return typeface;
    }
}
