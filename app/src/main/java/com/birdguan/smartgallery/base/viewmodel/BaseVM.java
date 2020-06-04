package com.birdguan.smartgallery.base.viewmodel;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.birdguan.smartgallery.base.ObservableAction;
import com.birdguan.smartgallery.base.uiaction.ClickUIAction;
import com.birdguan.smartgallery.base.uiaction.UIActionManager;
import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.util.ObserverParamMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.birdguan.smartgallery.base.uiaction.UIActionManager.CLICK_ACTION;


/**
 * @Author: birdguan
 * @Date: 2020/5/25 11:42
 */
public abstract class BaseVM extends ViewModel {
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

    protected Flowable<Integer> getDefaultClickThrottleFlowable() {
        return mUIActionManager
                .<ClickUIAction>getDefaultThrottleFlowable(CLICK_ACTION)
                .map(ClickUIAction::getLastEventListenerPosition);
    }

    protected Flowable<Integer> getDefaultClickThrottleFlowable(int throttleMilliseconds) {
        return mUIActionManager
                .<ClickUIAction>getDefaultThrottleFlowable(throttleMilliseconds, CLICK_ACTION)
                .map(ClickUIAction::getLastEventListenerPosition);
    }

    public void showToast(String message) {
        mShowToastListener.set(ObserverParamMap.setToastMessage(message));
    }

    public void checkEventListenerList(int eventListenerPosition) {
        if (eventListenerPosition >= mEventListenerList.size() || eventListenerPosition < 0) {
            throw new RuntimeException("ListenerList数组越界");
        }
        MyLog.d(TAG, "checkEventListenerList", "状态:class:eventListenerPosition:",
                "", getRealClassName(), eventListenerPosition);
    }

    public List<ObservableField<? super Object>> getEventListenerList() {
        return mEventListenerList;
    }

    public ObservableField<? super Object> getShowToastListener() {
        return mShowToastListener;
    }

    public String getRealClassName() {
        return getClass().getSimpleName();
    }

    public void initListener(ChildBaseVM childBaseVM, ObservableAction observableAction,
                             Integer... listenerPositions) {
        if (childBaseVM == null) {
            throw new RuntimeException("被监听的childVM为null");
        }
        if (observableAction == null) {
            throw new RuntimeException("被监听的行为为null");
        }
        if (listenerPositions == null) {
            throw new RuntimeException("传入的监听position为null");
        }

        // 监听某个childBaseVM的变化，以运行observableAction
        Flowable.fromArray(listenerPositions)
                .filter(position -> {
                    MyLog.d(TAG, "initListener",
                            "状态:position:childBaseVM.mClickListenerList.size():",
                            "过滤监听器", position, childBaseVM.getEventListenerList().size());
                    return !(position == null || position >= childBaseVM.getEventListenerList().size());
                })
                .map((Function<Integer, ObservableField<? super Object>>) childBaseVM.getEventListenerList()::get)
                .subscribe(observableField -> {
                    Observable.OnPropertyChangedCallback onPropertyChangedCallback = new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable sender, int propertyId) {
                            observableAction.onPropertyChanged(sender, propertyId);
                        }
                    };
                    registered(observableField, onPropertyChangedCallback);
                    observableField.addOnPropertyChangedCallback(onPropertyChangedCallback);
                });
    }

    private void registered(ObservableField observableField,
                            Observable.OnPropertyChangedCallback onPropertyChangedCallback) {
        if (mRegisteredViewModelFieldObserverMap == null) {
            mRegisteredViewModelFieldObserverMap = new HashMap<>();
        }
        mRegisteredViewModelFieldObserverMap.put(observableField, onPropertyChangedCallback);
    }

    @Override
    protected void onCleared() {
        if (mRegisteredViewModelFieldObserverMap != null) {
            for (ObservableField observableField : mRegisteredViewModelFieldObserverMap.keySet()) {
                observableField.removeOnPropertyChangedCallback(mRegisteredViewModelFieldObserverMap.get(observableField));
            }
            mRegisteredViewModelFieldObserverMap.clear();
        }
    }

    public boolean isNeedDestroy() {return true;}

    public void isEventEnable(boolean isEnable) {
        mUIActionManager.setEnable(isEnable);
    }

    public void beJoinedPreActionToEvent(UIActionManager.PreEventAction preEventAction) {
        mUIActionManager.setPreEventAction(preEventAction);
    }

    public void beJoinenAfterActionToEvent(UIActionManager.AfterEventAction afterEventAction) {
        mUIActionManager.setAfterEventAction(afterEventAction);
    }
}
