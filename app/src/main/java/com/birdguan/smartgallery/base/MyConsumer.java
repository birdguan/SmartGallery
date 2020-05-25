package com.birdguan.smartgallery.base;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 21:38
 */
public interface MyConsumer<IN, OUT> {
    OUT onNewResult(IN oldResult);
    void onFailure(Throwable throwable);
    void onCancellation();
}
