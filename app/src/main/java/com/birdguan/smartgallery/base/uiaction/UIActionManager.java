package com.birdguan.smartgallery.base.uiaction;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.viewmodel.BaseVM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 12:10
 */
public class UIActionManager {
    public static final String TAG = "SmartGallery: UIActionManager";
    public static final int DEFAULT_THROTTLE_MILLISECONDS = 400;
    public static final int CLICK_ACTION = 0;
    public static final int ITEM_SELECTED_ACTION = 1;
    public static final int PROGRESS_CHANGED_ACTION = 2;
    public static final int TEXT_CHANGED_ACTION = 3;

    private boolean isEnable = true;

    private List<PreEventAction> mPreEventActionList = new ArrayList<>();
    private List<AfterEventAction> mAfterEventActionList = new ArrayList<>();

    @SuppressLint("UseSparseArrays")
    private static final Map<Integer, Class<? extends UIAction>> UIActionClassMap = new HashMap<>();

    static {
        // 添加新的UIAction
        UIActionClassMap.put(CLICK_ACTION, )
    }

    private BaseVM mBaseVM;

    @SuppressLint("UseSparseArrays")
    private final Map<Integer, UIAction> mUIActionMap = new HashMap<>();

    public UIActionManager(BaseVM baseVM) {mBaseVM = baseVM;}

    public UIActionManager(BaseVM baseVM, Integer... uiActionTypes) {
        mBaseVM = baseVM;
        for (Integer type : uiActionTypes) {
            UIAction uiAction = initUIAction(type);
            if (uiAction == null) {
                throw new RuntimeException("暂时不支持这种uiAction");
            }
            //TODO: 这句似乎没有必要
            mUIActionMap.put(type, uiAction);
            MyLog.d(TAG, "UIActionManager", "状态:uiAction:type",
                    "", uiAction, type);
        }
    }



    private UIAction initUIAction(int type) {
        Class<? extends UIAction> uiActionClass = UIActionClassMap.get(type);
        if (uiActionClass == null) {
            return null;
        }
        try {
            return uiActionClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("使用Class构建UIAction对象失败");
        }
    }




    public interface PreEventAction {
        void doPreAction(int eventListenerPosition, int uiActionFlag, BaseVM baseVM, Object... params);
    }

    public interface AfterEventAction {
        void doAfterAction(int eventListenerPosition, int uiActionFlag, BaseVM baseVM, Object... params);
    }

    public interface CallAllPreEventAction {
        void callAllPreEventAction();
    }

    public interface CallAllAfterEventAction {
        void callAllAfterEventAction();
    }

    public void setPreEventAction(PreEventAction preEventAction) {

    }
}
