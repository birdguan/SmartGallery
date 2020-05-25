package com.birdguan.smartgallery.base.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 17:31
 */
public abstract class ItemManagerBaseVM <T extends ItemBaseVM> extends ChildBaseVM {
    private static final String TAG = "SmartGallery: ItemManagerBaseVM";
    public static final int CLICK_ITEM = 0;

    public final ObservableList<T> mDataItemList = new ObservableArrayList<>();
    public final ObservableField<Integer> mSelectedPosition = new ObservableField<>(-1);
    public final
}
