package com.birdguan.smartgallery.impl;

import com.birdguan.smartgallery.base.MyConsumer;
import com.birdguan.smartgallery.base.util.MyLog;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/4 10:10
 */
public abstract class BaseMyConsumer implements MyConsumer<Mat, Mat> {
    private static final String TAG = "SmartGallery: BaseMyConsumer";

    protected boolean isSaveNowResult = false;
    private Mat mNowResult;

    @Override
    public Mat onNewResult(Mat oldResult) {
        try {
            if (mNowResult != null && isSaveNowResult) {
                return mNowResult.clone();
            }
            Mat nowResult = onNewResultImpl(oldResult);
            if (isSaveNowResult) {
                mNowResult = nowResult.clone();
            }
            return nowResult;
        } catch (Exception e) {
            onUnhandledException(e);
        }
        return null;
    }

    @Override
    public void onFailure(Throwable throwable) {
        try {
            onFailureImpl(throwable);
        } catch (Exception e) {
            onUnhandledException(e);
        }
    }

    @Override
    public void onCancellation() {
        try {
            onCancellationImpl();
        } catch (Exception e) {
            onUnhandledException(e);
        }
    }

    public Mat getNowResult() {
        return mNowResult;
    }

    public void setNowResult(Mat nowResult) {
        this.mNowResult = nowResult;
    }

    public void copy(BaseMyConsumer baseMyConsumer) {
        MyLog.d(TAG, "copy", "状态:beCopyConsumer:",
                "拷贝", baseMyConsumer);
        if (baseMyConsumer == null) {
            throw new RuntimeException("传入的被拷贝的consumer为null");
        }
    }

    public boolean isNeedRun(BaseMyConsumer nextMyConsumer) {
        MyLog.d(TAG, "isNeedRun", "状态:nextMyConsumer:",
                "判断是否需要运行该consumer", nextMyConsumer);
        if (nextMyConsumer == null) {
            throw new RuntimeException("传入的被拷贝的consumer为null");
        }
        return true;
    }

    public boolean canRunNow(BaseMyConsumer nextNowMyConsumer) {
        MyLog.d(TAG, "isNeedRun", "状态:nextNowMyConsumer:",
                "判断是否可以重新运行当前的consumer", nextNowMyConsumer);
        if (nextNowMyConsumer == null) {
            throw new RuntimeException("传入的被拷贝的consumer为null");
        }
        return false;
    }

    public abstract Mat onNewResultImpl(Mat oldResult);

    public abstract void onFailureImpl(Throwable t);

    public abstract void onCancellationImpl();

    public String GetRealName() {
        return getClass().getSimpleName();
    }

    private void onUnhandledException(Exception e) {
        MyLog.d(TAG, "onUnhandledException", "class:e.getMssage():e.toString:",
                this.getClass().getName(), e.getMessage(), e.toString());
    }

    protected void destroy() {
        if (isSaveNowResult && mNowResult != null) {
            mNowResult.release();
        }
    }
}
