package com.birdguan.smartgallery.impl;

import android.annotation.SuppressLint;
import android.util.Log;

import com.birdguan.smartgallery.base.MyConsumer;
import com.birdguan.smartgallery.base.util.MyLog;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/4 10:10
 */
public abstract class BaseMyConsumer implements MyConsumer<Mat, Mat> {
    private static final String TAG = "SmartGallery/BaseMyConsumer";

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
    public synchronized void onFailure(Throwable throwable) {
        try {
            onFailureImpl(throwable);
        } catch (Exception e) {
            onUnhandledException(e);
        }
    }

    @Override
    public synchronized void onCancellation() {
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
        MyLog.d(TAG, "copy", "状态:beCopyConsumer:" , "拷贝" ,  baseMyConsumer);

        if (baseMyConsumer == null) {
            throw new RuntimeException("传入的被拷贝的 consumer 为null");
        }
    }

    public boolean isNeedRun(BaseMyConsumer nextMyConsumer) {
        MyLog.d(TAG, "isNeedRun", "状态:nextMyConsumer:this" , "判断是否需要运行该consumer" ,  nextMyConsumer , this);

        if (nextMyConsumer == null) {
            throw new RuntimeException("传入的 consumer 为null");
        }
        return true;
    }

    public boolean canRunNow(BaseMyConsumer nextNowMyConsumer) {
        MyLog.d(TAG, "canRunNow", "状态:nextNowMyConsumer:", "判断是否可以重新运行当前的consumer" , nextNowMyConsumer);

        if (nextNowMyConsumer == null) {
            throw new RuntimeException("传入的 consumer 为null");
        }
        return false;
    }

    protected abstract Mat onNewResultImpl(Mat oldResult);

    protected abstract void onFailureImpl(Throwable t);

    protected abstract void onCancellationImpl();

    public String getRealName() {
        return getClass().getSimpleName();
    }

    @SuppressLint("LongLogTag")
    protected void onUnhandledException(Exception e) {
        MyLog.d(TAG, "onUnhandledException", "class:e.getMessage():e.toString():" , this.getClass().getName() , e.getMessage() , e.toString());
        Log.d("SmartGallery/BaseMyConsumer" + this.getClass() , ("Consumer 抛出异常:" + e.getMessage() + " " + e.toString()));
    }

    protected void destroy() {
        if (isSaveNowResult && mNowResult != null) {
            mNowResult.release();
        }
    }
}
