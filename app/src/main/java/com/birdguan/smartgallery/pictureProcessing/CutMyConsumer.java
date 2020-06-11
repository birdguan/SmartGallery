package com.birdguan.smartgallery.pictureProcessing;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.impl.BaseMyConsumer;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

/**
 * @Author: birdguan
 * @Date: 2020/6/4 15:43
 */
public class CutMyConsumer extends BaseMyConsumer {
    private static final String TAG = "SmartGallery/BaseMyConsumer";

    private Rect mRect;

    public CutMyConsumer(Rect rect) {
        mRect = rect;
    }
    @Override
    public Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult",
                "运行", oldResult);
        if (oldResult == null) {
            throw new RuntimeException("被剪裁的Mat，不可为null");
        }
        if (mRect == null) {
            throw new RuntimeException("需要剪裁的区域Rect不可为null");
        }
        Mat newResult = new Mat();
        cut(oldResult.nativeObj, newResult.nativeObj, mRect.x, mRect.y, mRect.width, mRect.height);

        MyLog.d(TAG, "onNewResultImpl", "状态:newResult:",
                "运行完毕", newResult);
        return newResult;
    }

    @Override
    public void onFailureImpl(Throwable t) {

    }

    @Override
    public void onCancellationImpl() {

    }

    @Override
    public void copy(BaseMyConsumer baseMyConsumer) {
        super.copy(baseMyConsumer);
        if (!(baseMyConsumer instanceof CutMyConsumer)) {
            throw new RuntimeException("被拷贝的consumer需要和拷贝的consumer类型一致");
        }
        CutMyConsumer beCopyConsumer = (CutMyConsumer) baseMyConsumer;
        this.mRect.set(new double[]{
                beCopyConsumer.mRect.x,
                beCopyConsumer.mRect.y,
                beCopyConsumer.mRect.width,
                beCopyConsumer.mRect.height
        });
    }

    @Override
    public boolean isNeedRun(BaseMyConsumer nextMyConsumer) {
        super.isNeedRun(nextMyConsumer);

        if (!(nextMyConsumer instanceof CutMyConsumer)) {
            MyLog.d(TAG, "isNeedRun", "状态:",
                    "类型不同，需要徐云");
            return true;
        }

        Rect nextRect = ((CutMyConsumer) nextMyConsumer).mRect;
        if (Math.abs(nextRect.width - mRect.width) >= 2 || Math.abs(nextRect.height - mRect.height) >= 2){
            MyLog.d(TAG, "isNeedRun", "状态:nextRect:mRect",
                    "类型一致，而且长宽变化幅度都超过了2，需要运行", nextRect, mRect);
            return true;
        }
        MyLog.d(TAG, "isNeedRun", "状态:nextRect:mRect",
                "类型一致，而且长宽变化幅度都没有超过2，不需要运行", nextRect, mRect);
        return false;
    }

    public native void cut(long inMatAddr, long outMatAddr, int x, int y, int width, int height);

    public Rect getRect() {
        return mRect;
    }

    public void setRect(Rect rect) {
        mRect = rect;
    }

    @Override
    public String toString() {
        return "CutMyConsumer{" +
                "mRect=" + mRect +
                '}';
    }
}
