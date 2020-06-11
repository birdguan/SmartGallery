package com.birdguan.smartgallery.base.uiaction;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.viewmodel.BaseVM;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 14:39
 */
public class ClickUIAction extends BaseUIAction {
    private static final String TAG = "SmartGallery/ClickUIAction";
    private  UIActionListener<ClickUIAction> mOnClickListener = null;

    @Override
    public void onTriggerListener(int eventListenerPosition, BaseVM baseVM,
                                  UIActionManager.CallAllPreEventAction callAllPreEventAction,
                                  UIActionManager.CallAllAfterEventAction callAllAfterEventAction,
                                  Object... params) {
        super.onTriggerListener(eventListenerPosition, baseVM,
                callAllPreEventAction,
                callAllAfterEventAction,
                params);
        mLastEventListenerPosition = eventListenerPosition;
        mCallAllAfterEventAction = callAllAfterEventAction;

        if (mOnClickListener != null) {
            mOnClickListener.onUIActionChanged(this);
        }

        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition",
                "触发了点击事件监听器", eventListenerPosition);
    }

    @Override
    public boolean checkParams(Object[] params) {
        return true;
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mOnClickListener = (UIActionListener<ClickUIAction>) listener;
    }

    @Override
    public String toString() {
        return "ClickUIAction{" +
                "mOnClickListener=" + mOnClickListener +
                '}';
    }
}
