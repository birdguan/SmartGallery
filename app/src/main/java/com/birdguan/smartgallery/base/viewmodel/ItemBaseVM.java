package com.birdguan.smartgallery.base.viewmodel;

import androidx.databinding.ObservableField;

import com.birdguan.smartgallery.base.util.ObserverParamMap;

import java.util.List;

import static com.birdguan.smartgallery.staticParam.ObserverMapKey.ItemBaseVM_mPosition;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 16:03
 */
public abstract class ItemBaseVM extends ChildBaseVM{
    private static final String TAG = "SmartGallery/ItemBaseVM";

    protected Integer mPosition = -1;
    public final ObservableField<Boolean> isSelected = new ObservableField<>(false);

    public ItemBaseVM(List<ObservableField<? super Object>> eventListenerList, Integer position) {
        super(eventListenerList);
        mPosition = position;
    }

    public ItemBaseVM(int listenerSize, Integer position) {
        super(listenerSize);
        mPosition = position;
    }

    @Override
    public void resume() {
        super.resume();
        isSelected.set(true);
    }

    @Override
    public void stop() {
        super.stop();
        isSelected.set(false);
    }

    public ItemBaseVM(Integer position) {
        mPosition = position;
    }

    public Integer getPosition() {
        return mPosition;
    }

    public void setPosition(Integer position) {
        mPosition = position;
    }

    @Override
    public boolean isNeedDestroy() {
        return false;
    }

    protected ObserverParamMap getPositionParamMap() {
        return ObserverParamMap.staticSet(ItemBaseVM_mPosition , mPosition);
    }
}
