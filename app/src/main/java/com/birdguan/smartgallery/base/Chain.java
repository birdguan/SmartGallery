package com.birdguan.smartgallery.base;

import com.facebook.imagepipeline.producers.BaseConsumer;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 21:41
 */
public interface Chain<T, M> {
    void init(T startParam);
    M runStart(BaseConsumer... consumers);
    M runNext(BaseConsumer consumer);
    M runNow(BaseConsumer consumer);
    M undo();
    M redo();
    void destroy();
}
