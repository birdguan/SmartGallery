package com.birdguan.smartgallery.base;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.util.ObserverParamMap;
import com.birdguan.smartgallery.base.viewmodel.BaseVM;
import com.birdguan.smartgallery.base.viewmodel.ChildBaseVM;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * @Author: birdguan
 * @Date: 2020/5/26 9:12
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "SmartGallery/BaseActivity";

    private Map<ObservableField, Observable.OnPropertyChangedCallback> mRegisteredViewModelFieldObserverMap
            = new HashMap<>();

    public void initListener(ChildBaseVM childBaseVM,
                             ObservableAction observableAction,
                             Integer... listenerPositions) {
        if (childBaseVM == null) {
            throw new RuntimeException("被监听的childVM为null");
        }
        if (observableAction == null) {
            throw new RuntimeException("被监听的行为为null");
        }
        if (listenerPositions == null) {
            throw new RuntimeException("被传入的监听器的position为null");
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

    private void registered(ObservableField observableField , Observable.OnPropertyChangedCallback onPropertyChangedCallback) {
        mRegisteredViewModelFieldObserverMap.put(observableField , onPropertyChangedCallback);
    }

    protected void showToast(BaseVM baseVM) {
        if (baseVM == null) {
            throw new RuntimeException("被监听的ViewModel不可为null");
        }

        Observable.OnPropertyChangedCallback onPropertyChangedCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                Toast.makeText(BaseActivity.this, ObserverParamMap.getToastMessage(observable) , Toast.LENGTH_SHORT).show();
            }
        };

        registered(baseVM.getShowToastListener() , onPropertyChangedCallback);
        baseVM.getShowToastListener().addOnPropertyChangedCallback(onPropertyChangedCallback);
    }

    protected void showToast(String message) {
        Toast.makeText(BaseActivity.this, message , Toast.LENGTH_SHORT).show();
    }

    public <T extends ViewModel> T getViewModel(@NonNull Class<T> modelClass)  {
        return ViewModelProviders.of(this).get(modelClass);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (ObservableField observableField : mRegisteredViewModelFieldObserverMap.keySet()) {
            observableField.removeOnPropertyChangedCallback(mRegisteredViewModelFieldObserverMap.get(observableField));
        }
        mRegisteredViewModelFieldObserverMap.clear();
    }

    protected void registerViewModelFieldsObserver() {}
}
