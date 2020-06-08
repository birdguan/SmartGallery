package com.birdguan.smartgallery.viewModel.itemManagerVM;

import androidx.databinding.ObservableField;

import com.birdguan.smartgallery.BR;
import com.birdguan.smartgallery.R;
import com.birdguan.smartgallery.base.IImageUriFetch;
import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.util.MyUtil;
import com.birdguan.smartgallery.base.util.ObserverParamMap;
import com.birdguan.smartgallery.base.viewmodel.ItemBaseVM;
import com.birdguan.smartgallery.base.viewmodel.ItemManagerBaseVM;
import com.birdguan.smartgallery.impl.SystemImageUriFetch;

import java.util.List;

import io.reactivex.Flowable;

import static com.birdguan.smartgallery.staticParam.ObserverMapKey.PictureItemManagerVM_mImageUri;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:25
 */
public class PictureItemManagerVM extends ItemManagerBaseVM<PictureItemManagerVM> {
    private static final String TAG = "SmartGalley: PictureItemManagerVM"
    public static final int ITEM_PICTURE_RESIZE_WIDTH = 100;
    public static final int ITEM_PICTURE_RESIZE_HEIGHT = 100;
    public static final int MENU_ITEM_WIDTH = MyUtil.getDisplayWidthDp() / 3 - MyUtil.px2dip(10);
    public static final int MENU_ITEM_HEIGHT = MENU_ITEM_WIDTH;

    private final IImageUriFetch mIImageUriFetch;

    public PictureItemManagerVM() {
        super(1 , BR.viewModel , R.layout.activity_main_picture_item);
        mIImageUriFetch = SystemImageUriFetch.getInstance();

        initItemVM();
    }

    @Override
    protected void initItemVM() {
        freshPictureList(mIImageUriFetch.getAllImageUriList());
    }

    public void freshPictureList(String directoryName) {
        freshPictureList(mIImageUriFetch.getALlImageUriListFromTag(directoryName));
    }

    private void freshPictureList(List<String> imageUriList) {
        mDataItemList.clear();
        final int[] nowPosition = {0};
        Flowable.fromIterable(imageUriList)
                .map(imageUri -> new PictureItemVM(mEventListenerList , nowPosition[0]++ , imageUri))
                .subscribe(mDataItemList::add);
    }

    public static class PictureItemVM extends ItemBaseVM {
        public final ObservableField<String> mImageUri=new ObservableField<>();

        public PictureItemVM(List<ObservableField<? super Object>> clickListenerList, int position , String imageUri ) {
            super(clickListenerList, position);
            initDefaultUIActionManager();

            mImageUri.set(imageUri);
            initClick();
        }

        private void initClick() {
            getDefaultClickThrottleFlowable(3000)
                    .subscribe(LastEventListenerPosition -> {
                        ObserverParamMap paramMap = getPositionParamMap()
                                .set(PictureItemManagerVM_mImageUri , mImageUri.get());
                        mEventListenerList.get(LastEventListenerPosition).set(paramMap);
                        MyLog.d(TAG, "onTextChanged", "状态:LastEventListenerPosition:ObserverParamMap:", LastEventListenerPosition , paramMap);
                    });
        }
    }
}
