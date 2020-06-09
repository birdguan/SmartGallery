package com.birdguan.smartgallery.pictureProcessing;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.impl.BaseMyConsumer;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:41
 */
public class WhiteBalanceMyConsumer extends BaseMyConsumer {
    private static String TAG = "SmartGallery: WhiteBalanceMyConsumer";

    @Override
    public void copy(BaseMyConsumer baseMyConsumer) {
        super.copy(baseMyConsumer);
        if (!(baseMyConsumer instanceof  CutMyConsumer)) {
            throw new RuntimeException("被拷贝的 consumer 需要和拷贝的 consumer 类型一致");
        }
    }

    @Override
    public Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult:" , "运行" , oldResult);

        if (oldResult == null) {
            throw new RuntimeException("被自动白平衡的Mat 不可为null");
        }

        Mat newResult = new Mat();
        whiteBalance(oldResult.nativeObj , newResult.nativeObj);

        MyLog.d(TAG, "onNewResultImpl", "状态:newResult:" , "运行完毕" , newResult);
        return newResult;
    }

    @Override
    public void onFailureImpl(Throwable t) {

    }

    @Override
    public void onCancellationImpl() {

    }

    private native void whiteBalance(long in_mat_addr , long out_mat_addr);

}
