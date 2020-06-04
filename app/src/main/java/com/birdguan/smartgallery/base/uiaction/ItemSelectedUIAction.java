package com.birdguan.smartgallery.base.uiaction;

import androidx.databinding.ObservableList;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.viewmodel.BaseVM;
import com.birdguan.smartgallery.base.viewmodel.ItemBaseVM;
import com.birdguan.smartgallery.base.viewmodel.ItemManagerBaseVM;

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
        ItemManagerBaseVM<ItemBaseVM> itemManagerBaseVM = (ItemManagerBaseVM<ItemBaseVM>) baseVM;
        ObservableList<ItemBaseVM> dataItemList = itemManagerBaseVM.mDataItemList;
        if (dataItemList.size() <= position) {
            MyLog.d(TAG, "onItemSelected", "状态:position:dataItemList",
                    "", position, dataItemList);
            throw new RuntimeException("被选择的目录position不能大于等于目录总数");
        }

        ItemBaseVM itemBaseVM = dataItemList.get(position);
        if (itemBaseVM == null) {
            throw new RuntimeException("被选择的目录不可为null");
        }

        mSelectedItemPostion = position;
        mLastEventListenerPosition = eventListenerPosition;

        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.onUIActionChanged(this);
        }

        MyLog.d(TAG, "onTriggerListener", "状态:eventListenerPosition:position",
                "触发了点击事件监听器", eventListenerPosition, position);
    }

    public int getSelectedItemPosition() {
        if (mSelectedItemPostion == -1) {
            throw new RuntimeException("还没有触发过该事件");
        }
        return mSelectedItemPostion;
    }

    @Override
    public boolean checkParams(Object[] params) {
        return (params != null && params.length >= 1 && params[0] != null && (Integer)params[0] >= 0);
    }

    @Override
    public void setListener(UIActionListener<? extends UIAction> listener) {
        mOnItemSelectedListener = (UIActionListener<ItemSelectedUIAction>) listener;
    }

    @Override
    public String toString() {
        return "ItemSelectedUIAction{" +
                "mOnItemSelectedListener=" + mOnItemSelectedListener +
                ", mSelectedItemPostion=" + mSelectedItemPostion +
                '}';
    }
}
