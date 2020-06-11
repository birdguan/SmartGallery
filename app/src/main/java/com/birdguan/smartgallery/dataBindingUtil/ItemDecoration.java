package com.birdguan.smartgallery.dataBindingUtil;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 17:58
 */
public class ItemDecoration {
    private static final String TAG = "SmartGallery/ItemDecoration";

    public static RecyclerView.ItemDecoration defaultGridLayoutItemDecoration() {
        return new RecyclerView.ItemDecoration(){
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(5 , 5 , 5 , 5);
            }
        };
    }

    public static RecyclerView.ItemDecoration defaultItemDecoration() {
        return new RecyclerView.ItemDecoration(){
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(5 , 0 , 5 , 0);
            }
        };
    }
}
