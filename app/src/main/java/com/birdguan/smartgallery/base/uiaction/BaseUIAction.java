package com.birdguan.smartgallery.base.uiaction;

import com.birdguan.smartgallery.base.viewmodel.BaseVM;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 14:32
 */
public abstract class BaseUIAction implements UIAction {
    int mLastEventListenerPosition = -1;
    UIActionManager.CallAllAfterEventAction mCallAllAfterEventAction;
    private UIActionManager.CallAllPreEventAction mCallAllPreEventAction;

    @Override
    public void onTriggerListener(int eventListenerPosition, BaseVM baseVM,
                                  UIActionManager.CallAllPreEventAction callAllPreEventAction,
                                  UIActionManager.CallAllAfterEventAction callAllAfterEventAction,
                                  Object... params) {
        mCallAllAfterEventAction = callAllAfterEventAction;
        mCallAllPreEventAction = callAllPreEventAction;
    }

    public int getLastEventListenerPosition() {
        if (mLastEventListenerPosition == -1) {
            throw new RuntimeException("还未触发过该事件");
        }
        return mLastEventListenerPosition;
    }

    @Override
    public UIActionManager.CallAllAfterEventAction getCallAllAfterEventAction() {
        return mCallAllAfterEventAction;
    }

    @Override
    public UIActionManager.CallAllPreEventAction getCallAllPreEventAction() {
        return mCallAllPreEventAction;
    }
}
