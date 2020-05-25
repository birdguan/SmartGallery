package com.birdguan.smartgallery.base;

import java.util.Observable;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 21:39
 */
public interface ObservableAction {
    void onPropertyChanged(Observable observable, int i);
}
