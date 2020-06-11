package com.birdguan.smartgallery.base;

import androidx.databinding.ObservableField;

import com.birdguan.smartgallery.base.uiaction.UIActionManager;
import com.birdguan.smartgallery.base.util.MyUtil;
import com.birdguan.smartgallery.base.viewmodel.ItemBaseVM;
import com.birdguan.smartgallery.base.viewmodel.ItemManagerBaseVM;
import com.birdguan.smartgallery.viewModel.includeLayoutVM.PictureParamMenuVM;

import static com.birdguan.smartgallery.base.uiaction.UIActionManager.PROGRESS_CHANGED_ACTION;

/**
 * @Author: birdguan
 * @Date: 2020/5/26 14:54
 */
public abstract class BaseSeekBarRecycleViewVM <T extends ItemBaseVM> extends ItemManagerBaseVM<T> {
    private static final String TAG = "SmartGallery/BaseSeekBarRecycleViewVM";

    public static final int LEAVE_BSBRV_VM_LISTENER = 1;
    public static final int PROGRESS_CHANGE = 2;

    public static final int PROGRESS_MAX = 100;

    public static final int MENU_PADDING = 10;
    public static final int SEEK_BAR_HEIGHT = 26;
    public static final int MENU_HEIGHT = PictureParamMenuVM.MENU_HEIGHT;
    public static final int MENU_WIDTH = MyUtil.getDisplayWidthDp();
    public static final int MENU_ITEM_HEIGHT = PictureParamMenuVM.MENU_ITEM_HEIGHT;
    public static final int MENU_ITEM_WIDTH = MENU_ITEM_HEIGHT;

    public final ObservableField<Integer> mSelectParam = new ObservableField<>(PROGRESS_MAX / 2);

    public BaseSeekBarRecycleViewVM(int listenerSize, int viewModelId, int viewItemLayoutId) {
        super(listenerSize, viewModelId, viewItemLayoutId);
        initDefaultUIActionManager();
    }

    @Override
    protected void initDefaultUIActionManager() {
        mUIActionManager = new UIActionManager(this , PROGRESS_CHANGED_ACTION);
    }

    protected abstract void initClick();
    protected abstract void initProgressChanged();

    @Override
    public void resume() {
        super.resume();
        mSelectParam.set(PROGRESS_MAX / 2);
    }
}
