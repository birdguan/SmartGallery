package com.birdguan.smartgallery.base.uiaction;

import com.birdguan.smartgallery.base.viewmodel.BaseVM;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 14:21
 */
public interface UIAction {
    final String TAG = "SmartGallery: UIAction";
    boolean checkParams(Object[] params);
    interface UIActionListener<T> {
        void onUIActionChanged(T action);
    }

    void setListener(UIActionListener<? extends UIAction> listener);

    void onTriggerListener(int eventListenerPosition, BaseVM baseVM,
                           UIActionManager.CallAllPreEventAction callAllPreEventAction,
                           UIActionManager.CallAllAfterEventAction callAllAfterEventAction,
                           Object... params);
    UIActionManager.CallAllAfterEventAction getCallAllAfterEventAction();
    UIActionManager.CallAllPreEventAction getCallAllPreEventAction();
}
