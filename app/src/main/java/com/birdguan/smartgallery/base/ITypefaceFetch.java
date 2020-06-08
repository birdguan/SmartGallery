package com.birdguan.smartgallery.base;

import android.graphics.Typeface;

import com.birdguan.smartgallery.impl.LocalTypefaceFetch;
import com.birdguan.smartgallery.impl.SystemTypefaceFetch;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 21:05
 */
public interface ITypefaceFetch {
    List<ITypefaceFetch> mITypefaceFetch = new ArrayList<>();

    List<Typeface> getAllTypeface();
    List<String> getAllTypefaceName();
    Typeface getTypeface(String typefaceName);

    static void init() {
        SystemTypefaceFetch.getInstance();
        LocalTypefaceFetch.getInstance();
    }

    static Typeface getTypefaceFromAll(String typefaceName) {
        Typeface typeface;
        for (ITypefaceFetch iTypefaceFetch : mITypefaceFetch) {
            typeface = iTypefaceFetch.getTypeface(typefaceName);
            if (typeface != null) {
                return typeface;
            }
        }
        throw new RuntimeException("没有这种字体");
    }



    static void addITypefaceFetch(ITypefaceFetch iTypefaceFetch) {
        mITypefaceFetch.add(iTypefaceFetch);
    }
}
