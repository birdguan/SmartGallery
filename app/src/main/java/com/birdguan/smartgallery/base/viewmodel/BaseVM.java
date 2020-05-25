package com.birdguan.smartgallery.base.viewmodel;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.birdguan.smartgallery.base.uiaction.UIActionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.birdguan.smartgallery.base.uiaction.UIActionManager.CLICK_ACTION;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 11:42
 */
public class BaseVM extends ViewModel {
    public static final String TAG = "SmartGallery: BaseVM";

    private Map<ObservableField, Observable.OnPropertyChangedCallback> mRegisteredViewModelFieldObserverMap;
    protected final ObservableField<? super Object> mShowToastListener = new ObservableField<>();
    protected final List<ObservableField<? super Object>> mEventListenerList = new ArrayList<>();
    public UIActionManager mUIActionManager;

    public BaseVM(List<ObservableField<? super Object>> eventListenerList) {
        mEventListenerList.addAll(eventListenerList);
    }

    public BaseVM(int listenerSize) {
        for (int i = 0; i < listenerSize; i++) {
            mEventListenerList.add(new ObservableField<>());
        }
    }

    public BaseVM() {}

    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this, CLICK_ACTION);
    }

    public String getRealClassName() {
        return getClass().getSimpleName();
    }

    public boolean isNeedDestroy() {return true;}
}
