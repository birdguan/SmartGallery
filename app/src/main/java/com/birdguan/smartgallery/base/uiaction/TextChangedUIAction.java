package com.birdguan.smartgallery.base.uiaction;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.viewmodel.BaseVM;

/**
 * @Author: birdguan
 * @Date: 2020/5/26 11:45
 */
public class TextChangedUIAction extends BaseUIAction {
    private static final String TAG = "SmartGallery/TextChangedUIAction";
    private UIActionListener<TextChangedUIAction> mTextChangedListener = null;
    private CharSequence mNowText;

    @Override
    public void onTriggerListener(int eventListenerPosition, BaseVM baseVM, UIActionManager.CallAllPreEventAction callAllPreEventAction, UIActionManager.CallAllAfterEventAction callAllAfterEventAction, Object... params) {
        super.onTriggerListener(eventListenerPosition, baseVM, callAllPreEventAction, callAllAfterEventAction, params);

        CharSequence changedText = (CharSequence) params[0];
        mLastEventListenerPosition = eventListenerPosition;
        mNowText = changedText;
        if (mTextChangedListener != null) {
            mTextChangedListener.onUIActionChanged(this);
        }
        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition:changedText:",
                "触发了输入框文字变化事件", eventListenerPosition, changedText);
    }

    @Override
    public boolean checkParams(Object[] params) {
        return (params != null && params.length >= 1 && params[0] != null);
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mTextChangedListener = (UIActionListener<TextChangedUIAction>) listener;
    }

    public CharSequence getNowText() {
        if (mNowText == null) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mNowText;
    }

    @Override
    public String toString() {
        return "TextChangedUIAction{" +
                "mTextChangedListener=" + mTextChangedListener +
                ", mNowText=" + mNowText +
                '}';
    }
}
