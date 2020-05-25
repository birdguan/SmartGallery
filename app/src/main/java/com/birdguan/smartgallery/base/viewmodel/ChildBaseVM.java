package com.birdguan.smartgallery.base.viewmodel;

import androidx.databinding.ObservableField;

import com.birdguan.smartgallery.base.util.MyLog;

import java.util.List;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 16:04
 */
public class ChildBaseVM extends BaseVM {
    private static final String TAG = "SmartGallery: ChildBaseVM";
    private static final int RESUME = 1;
    private static final int STOP = 2;

    protected int mState = STOP;

    public ChildBaseVM(List<ObservableField<? super Object>> eventListenerList) {
        super(eventListenerList);
    }

    public ChildBaseVM(int listenerSize) {super(listenerSize);}

    public ChildBaseVM() {}

    public void resume() {
        mState = RESUME;
        MyLog.d(TAG, "resume", "状态:", "resume " + getRealClassName());
    }

    public void stop() {
        mState = STOP;
        MyLog.d(TAG, "stop", "状态:", "stop " + getRealClassName());
    }

    public int getState() {return mState;}

    public boolean isResume() {return mState == RESUME;}
}
