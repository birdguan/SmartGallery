package com.birdguan.smartgallery.pictureProcessing;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.impl.BaseMyConsumer;
import com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction;
import com.birdguan.smartgallery.pictureProcessing.filteraction.FilterAction;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:36
 */
public class PictureFilterMyConsumer extends BaseMyConsumer {
    private static String TAG = "SmartGallery: PictureFilterMyConsumer";

    private FilterAction mFilterAction;

    public PictureFilterMyConsumer(FilterAction filterAction) {
        mFilterAction = filterAction;
        if (mFilterAction instanceof AIFilterAction) {
            isSaveNowResult = true;
        }
    }

    public PictureFilterMyConsumer() {
    }

    @Override
    public void copy(BaseMyConsumer baseMyConsumer) {
        super.copy(baseMyConsumer);
        if (!(baseMyConsumer instanceof PictureFilterMyConsumer)) {
            throw new RuntimeException("被拷贝的 consumer 需要和拷贝的 consumer 类型一致");
        }

        PictureFilterMyConsumer beCopyConsumer = (PictureFilterMyConsumer) baseMyConsumer;
        mFilterAction = beCopyConsumer.mFilterAction;
    }

    @Override
    public boolean isNeedRun(BaseMyConsumer nextMyConsumer) {
        super.isNeedRun(nextMyConsumer);

        if (!(nextMyConsumer instanceof PictureFilterMyConsumer)) {
            MyLog.d(TAG, "isNeedRun", "状态:", "类型不同，需要运行");
            return true;
        }

        if (mFilterAction == ((PictureFilterMyConsumer) nextMyConsumer).mFilterAction) {
            MyLog.d(TAG, "isNeedRun", "状态:", "参数相同，不需要运行");
            return false;
        }

        return true;
    }

    @Override
    public boolean canRunNow(BaseMyConsumer nextNowMyConsumer) {
        super.canRunNow(nextNowMyConsumer);
        if (!(nextNowMyConsumer instanceof PictureFilterMyConsumer)) {
            MyLog.d(TAG, "canRunNow", "状态:nextNowMyConsumer", "传入的consumer类型不同，不能runNow" , nextNowMyConsumer.getRealName());
            return false;
        }

        if (((PictureFilterMyConsumer) nextNowMyConsumer).mFilterAction == null) {
            MyLog.d(TAG, "canRunNow", "状态:", "传入的滤镜为null，不能runNow");
            return false;
        }
        return true;
    }

    @Override
    public Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult:", "运行", oldResult);

        if (oldResult == null) {
            throw new RuntimeException("被添加图形框的参数的Mat 不可为null");
        }

        Mat newMat = new Mat();
        mFilterAction.filter(oldResult , newMat);
        return newMat;
    }

    @Override
    public void onFailureImpl(Throwable t) {

    }

    @Override
    public void onCancellationImpl() {

    }

}
