package com.birdguan.smartgallery.impl;

import android.graphics.Typeface;

import com.birdguan.smartgallery.base.ITypefaceFetch;
import com.birdguan.smartgallery.base.util.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 21:13
 */
public class SystemTypefaceFetch implements ITypefaceFetch {
    private static final String TAG = "SmartGallery/SystemTypefaceFetch";

    private static SystemTypefaceFetch mSystemTypefaceFetch;
    private static final Map<String, Typeface> mTypefaceMap = new HashMap<>();
    public static final List<String> mTypefaceNameList = new ArrayList<>();
    static {
        mTypefaceNameList.add("默认");
        mTypefaceNameList.add("粗体");
    }

    public static SystemTypefaceFetch getInstance() {
        if (mSystemTypefaceFetch == null) {
            synchronized (SystemImageUriFetch.class) {
                if (mSystemTypefaceFetch == null) {
                    mSystemTypefaceFetch = new SystemTypefaceFetch();
                    ITypefaceFetch.addITypefaceFetch(mSystemTypefaceFetch);
                }
            }
        }
        return mSystemTypefaceFetch;
    }

    private SystemTypefaceFetch() {}

    @Override
    public List<Typeface> getAllTypeface() {
        if (mTypefaceNameList.size() != mTypefaceMap.size()) {
            for (String typefaceName : mTypefaceNameList) {
                getTypeface(typefaceName);
            }
        }
        MyLog.d(TAG, "getAllTypeface", "状态:mTypefaceNameList:mTypefaceMap",
                " ", mTypefaceNameList, mTypefaceMap);
        return new ArrayList<>(mTypefaceMap.values());
    }

    @Override
    public List<String> getAllTypefaceName() {
        return mTypefaceNameList;
    }

    @Override
    public Typeface getTypeface(String typefaceName) {
        MyLog.d(TAG, "getTypeface", "状态:typefaceName", typefaceName);
        Typeface typeface = mTypefaceMap.get(typefaceName);
        if (typeface == null) {
            if (typefaceName.equals("默认")) {
                typeface = Typeface.DEFAULT;
            } else if (typefaceName.equals("粗体")) {
                typeface = Typeface.DEFAULT_BOLD;
            }
            mTypefaceMap.put(typefaceName, typeface);
        }
        return typeface;
    }
}
