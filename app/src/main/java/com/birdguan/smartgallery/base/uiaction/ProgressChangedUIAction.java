package com.birdguan.smartgallery.base.uiaction;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.viewmodel.BaseVM;

/**
 * @Author: birdguan
 * @Date: 2020/5/26 11:38
 */
public class ProgressChangedUIAction extends BaseUIAction {
    private static final String TAG = "SmartGallery/ProgressChangedUIAction";
    private UIAction.UIActionListener<ProgressChangedUIAction> mOnProgressChangedListener = null;
    private int mProgress = Integer.MIN_VALUE;

    @Override
    public void onTriggerListener(int eventListenerPosition, BaseVM baseVM, UIActionManager.CallAllPreEventAction callAllPreEventAction, UIActionManager.CallAllAfterEventAction callAllAfterEventAction, Object... params) {
        super.onTriggerListener(eventListenerPosition, baseVM, callAllPreEventAction, callAllAfterEventAction, params);
        int progress = (int) params[0];
        mProgress = progress;
        mLastEventListenerPosition = eventListenerPosition;

        if (mOnProgressChangedListener != null) {
            mOnProgressChangedListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition:progress:",
                "触发了点击事件监听器", eventListenerPosition, progress);
    }

    @Override
    public boolean checkParams(Object[] params) {
        return (params != null && params.length >= 1 && params[0] != null);
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mOnProgressChangedListener = (UIActionListener<ProgressChangedUIAction>) listener;
    }

    @Override
    public String toString() {
        return "ProgressChangedUIAction{" +
                "mOnProgressChangedListener=" + mOnProgressChangedListener +
                ", mProgress=" + mProgress +
                '}';
    }

    public int getProgress() {
        if (mProgress == Integer.MIN_VALUE) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mProgress;
    }
}
