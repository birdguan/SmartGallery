package com.birdguan.smartgallery.base.uiaction;

import com.birdguan.smartgallery.base.viewmodel.BaseVM;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 15:30
 */
public class ItemSelectedUIAction extends BaseUIAction {
    private static final String TAG = "SmartGallery: ItemSelectedAction";

    private UIActionListener<ItemSelectedUIAction> mOnItemSelectedListener = null;
    private int mSelectedItemPostion = -1;

    @Override
    public void onTriggerListener(int eventListenerPosition, BaseVM baseVM,
                                  UIActionManager.CallAllPreEventAction callAllPreEventAction,
                                  UIActionManager.CallAllAfterEventAction callAllAfterEventAction,
                                  Object... params) {
        super.onTriggerListener(eventListenerPosition, baseVM,
                callAllPreEventAction,
                callAllAfterEventAction,
                params);
        int position = (int) params[0];
        mCallAllAfterEventAction = callAllAfterEventAction;

    }

    @Override
    public boolean checkParams(Object[] params) {
        return false;
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {

    }
}
