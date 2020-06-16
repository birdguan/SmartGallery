package com.birdguan.smartgallery.viewModel.includeLayoutVM;

import android.net.Uri;

import androidx.databinding.ObservableField;

import com.birdguan.smartgallery.BR;
import com.birdguan.smartgallery.R;
import com.birdguan.smartgallery.base.BaseSeekBarRecycleViewVM;
import com.birdguan.smartgallery.base.uiaction.ClickUIAction;
import com.birdguan.smartgallery.base.uiaction.UIActionManager;
import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.util.ObserverParamMap;
import com.birdguan.smartgallery.base.viewmodel.ItemBaseVM;
import com.birdguan.smartgallery.pictureProcessing.PictureFilterMyConsumer;
import com.birdguan.smartgallery.pictureProcessing.StringConsumerChain;
import com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction;
import com.birdguan.smartgallery.pictureProcessing.filteraction.FilterAction;

import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;

import static com.birdguan.smartgallery.base.uiaction.UIActionManager.CLICK_ACTION;
import static com.birdguan.smartgallery.base.viewmodel.ItemManagerBaseVM.CLICK_ITEM;
import static com.birdguan.smartgallery.staticParam.ObserverMapKey.PictureFilterItemVM_CallAllAfterEventAction;
import static com.birdguan.smartgallery.staticParam.ObserverMapKey.PictureFilterItemVM_mFilterAction;
import static com.birdguan.smartgallery.staticParam.ObserverMapKey.PictureFilterItemVM_mat;
import static com.birdguan.smartgallery.staticParam.StaticParam.PICTURE_FILTER_SAMPLE_IMAGE;

/**
 * @Author: birdguan
 * @Date: 2020/6/9 10:09
 */
public class PictureFilterMenuVM extends BaseSeekBarRecycleViewVM<PictureFilterMenuVM.PictureFilterItemVM> {
    private static final String TAG = "SmartGallery/PictureFilterMenuVM";

    private final StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private final String mSampleImageUri;
    private boolean isRunNow = false;

    public PictureFilterMenuVM() {
        super(3 , BR.viewModel , R.layout.activity_picture_processing_picture_filter_item);

        mSampleImageUri = Uri.fromFile(new File(PICTURE_FILTER_SAMPLE_IMAGE)).toString();
        initItemVM();
        initClick();
        initProgressChanged();
    }

    @Override
    protected void initItemVM() {
        final int[] position = {0};
        Flowable.fromIterable(FilterAction.getAllFilterAction())
                .subscribe(filterAction -> {
                    if (filterAction instanceof AIFilterAction) {
                        mDataItemList.add(new PictureFilterItemVM(mEventListenerList ,
                                false ,
                                position[0]++ ,
                                filterAction ,
                                ((AIFilterAction) filterAction).getImageUri()));
                    } else {
                        mDataItemList.add(new PictureFilterItemVM(mEventListenerList ,
                                false ,
                                position[0]++,
                                filterAction,
                                mSampleImageUri));
                    }
                });
    }

    @Override
    protected void initClick() {
        initListener(this, (observable, i) -> {
            Mat mat = ObserverParamMap.staticGetValue(observable , PictureFilterItemVM_mat);
            if (mat != null) {
                return;
            }
            FilterAction filterAction = ObserverParamMap.staticGetValue(observable , PictureFilterItemVM_mFilterAction);
            UIActionManager.CallAllAfterEventAction callAllAfterEventAction =
                    ObserverParamMap.staticGetValue(observable , PictureFilterItemVM_CallAllAfterEventAction);

            PictureFilterMyConsumer pictureFilterMyConsumer = new PictureFilterMyConsumer(filterAction);
            Flowable<Mat> flowable;
            if (isRunNow) {
                flowable = mStringConsumerChain.rxRunNowConvenient(pictureFilterMyConsumer);
            } else {
                flowable = mStringConsumerChain.rxRunNextConvenient(pictureFilterMyConsumer);
                isRunNow = true;
            }
            flowable.subscribe(mat1 -> {
                ObserverParamMap observerParamMap = ObserverParamMap
                        .staticSet(PictureFilterItemVM_mat , mat1);
                mEventListenerList.get(CLICK_ITEM).set(observerParamMap);

                if (callAllAfterEventAction != null) {
                    callAllAfterEventAction.callAllAfterEventAction();
                }
                MyLog.d(TAG, "useFilter",
                        "状态:observerParamMap:",
                        "" , observerParamMap);
            });
            MyLog.d(TAG,
                    "initClick",
                    "状态:filterAction:isRunNow:isShowYesNo:",
                    "调用滤镜", filterAction , isRunNow , true);
        }, CLICK_ITEM);
    }

    @Override
    protected void initProgressChanged() {

    }

    @Override
    public void resume() {
        super.resume();
        isRunNow = false;
    }

    public void setRunNow(boolean runNow) {
        isRunNow = runNow;
    }

    public static class PictureFilterItemVM extends ItemBaseVM {
        public static final String TAG = "SmartGallery/PictureFilterItemVM";

        private boolean isAdd = false;
        public final FilterAction mFilterAction;
        public final String mSampleImageUri;

        public PictureFilterItemVM(List<ObservableField<? super Object>> clickItemListenerList ,
                                   boolean isAdd,
                                   Integer position,
                                   FilterAction filterAction,
                                   String sampleImageUri) {
            this(clickItemListenerList , position , filterAction , sampleImageUri);
            this.isAdd = isAdd;
        }

        public PictureFilterItemVM(List<ObservableField<? super Object>> clickItemListenerList,
                                   Integer position,
                                   FilterAction filterAction,
                                   String sampleImageUri) {
            super(clickItemListenerList , position);
            initDefaultUIActionManager();

            mSampleImageUri = sampleImageUri;
            mFilterAction = filterAction;
            initClickAction();
        }

        private void initClickAction() {
            mUIActionManager
                    .<ClickUIAction>getDefaultThrottleFlowable(CLICK_ACTION)
                    .filter(clickUIAction -> {
                        MyLog.d(TAG, "initClickAction",
                                "状态:isAdd:mFilterAction",
                                "判断当前的item是否是 add" , isAdd , mFilterAction);
                        return (!isAdd && mFilterAction != null);
                    }).subscribe(clickUIAction -> {
                ObserverParamMap observerParamMap = getPositionParamMap()
                        .set(PictureFilterItemVM_mFilterAction , mFilterAction)
                        .set(PictureFilterItemVM_CallAllAfterEventAction,
                                clickUIAction.getCallAllAfterEventAction());
                mEventListenerList.get(CLICK_ITEM).set(observerParamMap);
                MyLog.d(TAG, "initClickAction",
                        "状态:observerParamMap:clickPosition",
                        "" , observerParamMap , clickUIAction.getLastEventListenerPosition());
            });
        }

        @Override
        public String toString() {
            return "PictureFilterItemVM{" +
                    "isAdd=" + isAdd +
                    ", mPosition=" + mPosition +
                    ", mSampleImageUri=" + mSampleImageUri +
                    ", mFilterAction=" + mFilterAction +
                    '}';
        }
    }
}
