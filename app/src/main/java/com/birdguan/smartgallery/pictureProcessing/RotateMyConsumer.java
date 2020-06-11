package com.birdguan.smartgallery.pictureProcessing;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.impl.BaseMyConsumer;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:40
 */
public class RotateMyConsumer extends BaseMyConsumer {
    private static final String TAG = "SmartGallery/RotateMyConsumer";

    public static final double ROTATE_ANGLE_90 = 90.0;
    public static final double ROTATE_ANGLE_180 = 180.0;
    public static final double ROTATE_ANGLE_270 = 270.0;
    public static final double ROTATE_ANGLE_360 = 360.0;

    private boolean isClockwise = false;
    private double mAngle = 0;
    private double mScale = 1.0;

    public RotateMyConsumer(boolean isClockwise, double angle, double scale) {
        this.isClockwise = isClockwise;
        mAngle = angle;
        mScale = scale;
    }

    public RotateMyConsumer(double angle) {
        mAngle = angle;
    }

    public RotateMyConsumer(double angle , int times) {
        this.mAngle = angle * times;
    }

    @Override
    public Mat onNewResultImpl(Mat oldResult) {
        MyLog.d(TAG, "onNewResultImpl", "状态:oldResult:" , "运行" , oldResult);

        if (oldResult == null) {
            throw new IllegalArgumentException("被旋转的Mat 不可为null");
        }

        if (mAngle == 0) {
            return oldResult;
        }

        if (!isClockwise) {
            mAngle = 360 - mAngle;
        }

        Mat newResult = new Mat();
        rotate(oldResult.nativeObj , newResult.nativeObj , mAngle , mScale);

        MyLog.d(TAG, "onNewResultImpl", "状态:newResult:mAngle:mScale:" , "运行完毕" , newResult , mAngle , mScale );
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
        if (!(baseMyConsumer instanceof RotateMyConsumer)) {
            throw new RuntimeException("被拷贝的 consumer 需要和拷贝的 consumer 类型一致");
        }

        RotateMyConsumer beCopyConsumer = (RotateMyConsumer) baseMyConsumer;
        this.isClockwise = beCopyConsumer.isClockwise;
        this.mAngle = beCopyConsumer.mAngle;
        this.mScale = beCopyConsumer.mScale;
    }

    private native void rotate(long in_mat_addr , long out_mat_addr , double angle , double scale);

    @Override
    public String toString() {
        return "RotateMyConsumer{" +
                "isClockwise=" + isClockwise +
                ", mAngle=" + mAngle +
                ", mScale=" + mScale +
                '}';
    }
}
