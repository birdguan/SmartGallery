package com.birdguan.smartgallery.base;

import android.graphics.Typeface;

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
        SystemTypeFetch
    }

    static void addITypefaceFetch(ITypefaceFetch iTypefaceFetch) {
        mITypefaceFetch.add(iTypefaceFetch);
    }
}
