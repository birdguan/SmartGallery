package com.birdguan.smartgallery.base;

import com.birdguan.smartgallery.impl.BaseMyConsumer;
import com.facebook.imagepipeline.producers.BaseConsumer;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 21:41
 */
public interface Chain<T, M> {
    void init(T startParam);
    M runStart(BaseMyConsumer... consumers);
    M runNext(BaseMyConsumer consumer);
    M runNow(BaseMyConsumer consumer);
    M undo();
    M redo();
    void destroy();
}
