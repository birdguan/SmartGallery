package com.birdguan.smartgallery.viewModel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;

import androidx.databinding.ObservableField;

import com.birdguan.smartgallery.base.BaseSeekBarRecycleViewVM;
import com.birdguan.smartgallery.base.viewmodel.ChildBaseVM;
import com.birdguan.smartgallery.impl.BaseMyConsumer;
import com.birdguan.smartgallery.mete.CutView;
import com.birdguan.smartgallery.pictureProcessing.StringConsumerChain;
import com.birdguan.smartgallery.viewModel.includeLayoutVM.PictureParamMenuVM;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.birdguan.smartgallery.mete.CutView.SCALE_MODEL;
import static com.birdguan.smartgallery.staticParam.StaticParam.LOADING_GIF;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:43
 */
public class PictureProcessingActivityVM extends ChildBaseVM {
    private static final String TAG = "SmartGallery: PictureProcessingActivityVM";

    public static final int SELECT_PICTURE_FILTER = 0;
    public static final int SELECT_PICTURE_TRANSFORM = 1;
    public static final int SELECT_PICTURE_PARAM = 2;
    public static final int SELECT_PICTURE_FRAME = 3;
    public static final int SELECT_PICTURE_TEXT = 4;
    public static final int CLICK_UNDO = 5;
    public static final int CLICK_REDO = 6;
    public static final int CLICK_BACK = 7;
    public static final int CLICK_YES = 8;
    public static final int CLICK_NO = 9;
    public static final int CLICK_SHARE = 10;
    public static final int CLICK_SAVE = 11;

    public static final int CHILD_VM_mPictureFilterMenuVM = 0;
    public static final int CHILD_VM_mPictureTransformMenuVM = 1;
    public static final int CHILD_VM_mPictureParamMenuVM = 2;
    public static final int CHILD_VM_mPictureFrameMenuVM = 3;
    public static final int CHILD_VM_mPictureTextMenuVM = 4;

    public final ObservableField<Bitmap> mImageBitMap = new ObservableField<>();
    public final ObservableField<Integer> mSelectTab = new ObservableField<>(0);
    public final ObservableField<Integer> mCutViewModel = new ObservableField<>(SCALE_MODEL);
    public final ObservableField<Boolean> mCanUndo = new ObservableField<>(false);
    public final ObservableField<Boolean> mCanRedo = new ObservableField<>(false);
    public final ObservableField<Boolean> mIsShowYesNo = new ObservableField<>(false);
    public final ObservableField<Boolean> mIsInLoading = new ObservableField<>(false);
    public final ObservableField<String> mLoadingGifUri = new ObservableField<>(Uri.fromFile(new File(LOADING_GIF)).toString());
    public final ObservableField<String> mLoadingText = new ObservableField<>("处理中...");
    public final ObservableField<CutView.OnLimitRectChangedListener> mCutViewListener = new ObservableField<>();
    public final ObservableField<BaseSeekBarRecycleViewVM> mNowBaseSeekBarRecycleViewVM = new ObservableField<>();

    private StringConsumerChain mStringConsumerChain = StringConsumerChain.getInstance();
    private String mImagePath;

    public PictureProcessingActivityVM(String imageUri) {
        super(12);
        initDefaultUIActionManager();

        mImagePath = Uri.parse(imageUri).getPath();
        initChildBaseVM(PictureFilterMenuVM.class , CHILD_VM_mPictureFilterMenuVM);
        initChildBaseVM(PictureTransformMenuVM.class , CHILD_VM_mPictureTransformMenuVM);
        initChildBaseVM(PictureParamMenuVM.class , CHILD_VM_mPictureParamMenuVM);
        initChildBaseVM(PictureFrameMenuVM.class , CHILD_VM_mPictureFrameMenuVM);
        initChildBaseVM(PictureTextMenuVM.class , CHILD_VM_mPictureTextMenuVM);
        mNowBaseSeekBarRecycleViewVM.set(getPictureFilterMenuVM());

        mStringConsumerChain.init(mImagePath);
        mStringConsumerChain.rxRunStartConvenient((BaseMyConsumer) null).subscribe(this::showMat);

        initPictureAction();
        initClick();

        changeNowChildVM(getPictureFilterMenuVM());

        joinPreActionToMyAllVM((eventListenerPosition, uiActionFlag, baseVM, params) -> {
            if (uiActionFlag == CLICK_ACTION) {
                mIsInLoading.set(true);
                isEventEnable(false);

                if (baseVM instanceof PictureFilterMenuVM.PictureFilterItemVM) {
                    mLoadingText.set("AI算法处理中...");
                }
            }
            MyLog.d(TAG, "doPreAction", "状态:", "PictureProcessingActivityVM前置了操作");
        });

        joinAfterActionToMyAllVM((eventListenerPosition, uiActionFlag, baseVM, params) -> {
            if (uiActionFlag == CLICK_ACTION){
                mIsInLoading.set(false);
                isEventEnable(true);

                if (baseVM instanceof PictureFilterMenuVM.PictureFilterItemVM) {
                    mLoadingText.set("处理中...");
                }
            }
            MyLog.d(TAG, "doPreAction", "状态:", "PictureProcessingActivityVM后置了操作");
        });


        MyLog.d(TAG, "PictureProcessingActivityVM", "imageUri:", imageUri );
    }

    private void initPictureAction() {

        // 监听 图片滤镜 的点击操作
        initListener(getPictureFilterMenuVM() , (observable, i) -> {
            Mat mat = ObserverParamMap.staticGetValue(observable , PictureFilterItemVM_mat);
            if (mat == null) {
                return;
            }
            mIsShowYesNo.set(true);
            showMat(mat);
            MyLog.d(TAG, "initPictureAction", "状态:isShowYesNo:mat:", "" , true , mat);
        }, CLICK_ITEM);

        // 监听 图片变换 的操作以更新图片
        initListener(getPictureTransformMenuVM() ,
                (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureTransformMenuVM_mat)) ,
                SELECT_PICTURE_ROTATE ,
                SELECT_PICTURE_HORIZONTAL_FLIP ,
                SELECT_PICTURE_VERTICAL_FLIP ,
                SELECT_PICTURE_WHITE_BALANCE ,
                SELECT_PICTURE_CUT ,
                LEAVE_TRANSFORM_LISTENER);

        // 监听 图片参数变化 的操作以更新图片
        initListener(getPictureParamMenuVM() ,
                (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureParamMenuVM_mat)) ,
                SELECT_BRIGHTNESS ,
                SELECT_CONTRAST ,
                SELECT_SATURATION ,
                SELECT_TONAL ,
                PARAM_PROGRESS_CHANGE);

        // 监听 图片插入 的点击操作
        initListener(getPictureFrameMenuVM(), (observable, i) -> mIsShowYesNo.set(true), CLICK_ITEM);

        // 监听 图片插入 的图片更新
        initListener(getPictureFrameMenuVM(), (observable, i) -> showMat(ObserverParamMap.staticGetValue(observable , PictureFrameItemVM_mat)) , LEAVE_BSBRV_VM_LISTENER);

        // 监听 文字插入 的输入文字操作
        initListener(getPictureTextMenuVM() , (observable, i) -> mIsShowYesNo.set(true), TEXT_CHANGE);

        // 监听 图片插入 的图片更新
        initListener(getPictureTextMenuVM(), (observable, i) -> {
            Mat mat = ObserverParamMap.staticGetValue(observable , PictureTextItemVM_mat);
            if (mat == null) {
                return;
            }
            showMat(mat);
        }, LEAVE_BSBRV_VM_LISTENER);

        MyLog.d(TAG, "initPictureAction", "状态:", "监听图像变化初始化完毕");
    }

    private void initClick() {
        mUIActionManager
                .<ClickUIAction>getDefaultThrottleFlowable(CLICK_ACTION)
                .filter(clickUIAction -> {
                    int position = clickUIAction.getLastEventListenerPosition();
                    if (position == CLICK_UNDO) {
                        clickUndo(clickUIAction.getCallAllAfterEventAction());
                        return false;
                    } else if (position == CLICK_REDO) {
                        clickRedo(clickUIAction.getCallAllAfterEventAction());
                        return false;
                    } else if (position == CLICK_BACK) {
                        clickBack(position);
                        clickUIAction.getCallAllAfterEventAction().callAllAfterEventAction();
                        return false;
                    } else if (position == CLICK_YES) {
                        clickYes(clickUIAction.getCallAllAfterEventAction());
                        return false;
                    } else if (position == CLICK_NO) {
                        clickNo();
                        clickUIAction.getCallAllAfterEventAction().callAllAfterEventAction();
                        return false;
                    } else if (position == CLICK_SHARE) {
                        Flowable.fromArray(1)
                                .map(integer -> clickShare())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(intent -> {
                                    mEventListenerList.get(CLICK_SHARE).set(ObserverParamMap.staticSet(PictureProcessingActivityVM_intent , intent));
                                    clickUIAction.getCallAllAfterEventAction().callAllAfterEventAction();
                                });
                        return false;
                    } else if (position == CLICK_SAVE) {
                        Flowable.fromArray(1)
                                .map(integer -> clickSave())
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(imageDirectory -> {
                                    showToast("该图片被储存在：" + imageDirectory + " 文件夹中");
                                    clickUIAction.getCallAllAfterEventAction().callAllAfterEventAction();
                                });
                        return false;
                    }
                    return true;
                }).subscribe(clickUIAction -> {
            int position = clickUIAction.getLastEventListenerPosition();
            ChildBaseVM childBaseVM = getChildBaseVM(position);
            switch (position) {
                case SELECT_PICTURE_FILTER:
                    mCutViewModel.set(SCALE_MODEL);
                    mNowBaseSeekBarRecycleViewVM.set((BaseSeekBarRecycleViewVM) childBaseVM);

                    break;
                case SELECT_PICTURE_TRANSFORM:
                    mCutViewListener.set((CutView.OnLimitRectChangedListener) childBaseVM);
                    mCutViewModel.set(CUT_MODEL);

                    break;
                case SELECT_PICTURE_PARAM:
                    mCutViewModel.set(SCALE_MODEL);

                    break;
                case SELECT_PICTURE_FRAME:
                    mCutViewListener.set((CutView.OnLimitRectChangedListener) childBaseVM);
                    mCutViewModel.set(INSERT_IMAGE_MODEL);
                    mNowBaseSeekBarRecycleViewVM.set((BaseSeekBarRecycleViewVM) childBaseVM);

                    break;
                case SELECT_PICTURE_TEXT:
                    mCutViewModel.set(INSERT_TEXT_MODEL);
                    mNowBaseSeekBarRecycleViewVM.set((BaseSeekBarRecycleViewVM) childBaseVM);

                    break;
            }

            mSelectTab.set(position);
//                    if (mIsShowYesNo.get()) {
//                        clickYes();
//                    }
            changeNowChildVM(childBaseVM);
            clickUIAction.getCallAllAfterEventAction().callAllAfterEventAction();
        });
    }

    private void clickUndo(UIActionManager.CallAllAfterEventAction callAllAfterEventAction) {
        mStringConsumerChain
                .rxUndoConvenient()
                .subscribe(mat -> {
                    showMat(mat);
                    getPictureParamMenuVM().fresh();
                    callAllAfterEventAction.callAllAfterEventAction();
                    MyLog.d(TAG, "clickUndo", "状态:", "undo完毕");
                });
    }

    private void clickRedo(UIActionManager.CallAllAfterEventAction callAllAfterEventAction) {
        mStringConsumerChain
                .rxRedoConvenient()
                .subscribe(mat -> {
                    showMat(mat);
                    getPictureParamMenuVM().fresh();
                    callAllAfterEventAction.callAllAfterEventAction();
                    MyLog.d(TAG, "clickRedo", "状态:", "redo完毕");
                });
    }

    private void clickBack(int position) {
        mEventListenerList.get(position).set(ObserverParamMap.staticSet(PictureTransformMenuVM_position , CLICK_BACK));
    }

    private void clickYes(UIActionManager.CallAllAfterEventAction callAllAfterEventAction) {
        if (mNowChildBaseVM == getPictureFilterMenuVM()) {
            getPictureFilterMenuVM().setRunNow(false);
            callAllAfterEventAction.callAllAfterEventAction();
        } else if (mNowChildBaseVM == getPictureFrameMenuVM()) {
            getPictureFrameMenuVM().runInsertImage(callAllAfterEventAction);
            getPictureFrameMenuVM().mInsertImagePath.set("");
        } else if (mNowChildBaseVM == getPictureTextMenuVM()) {
            getPictureTextMenuVM().runInsertText(callAllAfterEventAction);
        }

        if (mNowChildBaseVM instanceof ItemManagerBaseVM) {
            ((ItemManagerBaseVM) mNowChildBaseVM).initSelectedPosition();
        }
        mIsShowYesNo.set(false);
    }

    private void clickNo() {
        if (mNowChildBaseVM == getPictureFilterMenuVM()) {
            getPictureFilterMenuVM().setRunNow(false);
            if (mCanUndo.get()) {
                showMat(mStringConsumerChain.cancelNowConsumer());
            }
        } else if (mNowChildBaseVM == getPictureFrameMenuVM()) {
            getPictureFrameMenuVM().mInsertImagePath.set("");
        } else if (mNowChildBaseVM == getPictureTextMenuVM()) {
            getPictureTextMenuVM().mText.set("");
        }

        if (mNowChildBaseVM instanceof ItemManagerBaseVM) {
            ((ItemManagerBaseVM) mNowChildBaseVM).initSelectedPosition();
        }
        mIsShowYesNo.set(false);
    }

    private Intent clickShare() {
        MyUtil.saveBitmap(mImageBitMap.get() , SHARE_IMAGE);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        File imageFile = new File(SHARE_IMAGE);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(PictureProcessingApplication.getAppContext() , "com.example.whensunset.pictureprocessinggraduationdesign.fileprovider", imageFile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(imageFile);
        }

        intent.putExtra(Intent.EXTRA_STREAM , data);
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, "分享了一张图片");


        MyLog.d(TAG, "clickShare", "状态:", "");
        return intent;
    }

    private String clickSave() {

        String imageName = mImagePath.substring(mImagePath.lastIndexOf("/") + 1);
        String imageDirectory = StaticParam.MY_PHOTO_SHOP_DIRECTORY;
        String[] imageNames = imageName.split("\\.");

        StringBuilder saveImagePath = new StringBuilder();
        saveImagePath.append(imageDirectory);
        saveImagePath.append("/");
        saveImagePath.append(imageNames[0]);
        saveImagePath.append(System.currentTimeMillis());
        saveImagePath.append(".");
        saveImagePath.append(imageNames[1]);
        MyUtil.saveBitmap(mImageBitMap.get() , saveImagePath.toString());


        ObservableField<? extends Object> observableField = mEventListenerList.get(CLICK_SAVE);
        observableField.set(null);
        observableField.notifyChange();
        MyLog.d(TAG, "clickShare", "状态:imageName:imageNames:imageDirectory:saveImagePath:",
                "" , imageName , imageNames ,imageDirectory , saveImagePath.toString());
        return imageDirectory;
    }

    private void showMat(Mat mat) {
        MyLog.d(TAG, "showMat", "状态:mat:", "进行图片展示" , mat);
        if (mat == null) {
            throw new RuntimeException("被展示的mat为null");
        }

        mCanUndo.set(mStringConsumerChain.canUndo());
        mCanRedo.set(mStringConsumerChain.canRedo());

        Bitmap oldBitmap = mImageBitMap.get();
        if (oldBitmap != null) {
            oldBitmap.recycle();
            oldBitmap = null;
            mImageBitMap.set(null);
        }

        Mat matRgba = MyUtil.matBgrToRgba(mat);
        Bitmap newBitMap = Bitmap.createBitmap(matRgba.cols() , matRgba.rows() , Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(matRgba , newBitMap);
        mImageBitMap.set(newBitMap);
        matRgba.release();

        MyLog.d(TAG, "showMat", "状态:mCanUndo:mCanRedo:", "图片展示完毕" , mCanUndo.get() , mCanRedo.get());
    }

    public PictureFilterMenuVM getPictureFilterMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureFilterMenuVM);
    }

    public PictureTransformMenuVM getPictureTransformMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureTransformMenuVM);
    }

    public PictureParamMenuVM getPictureParamMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureParamMenuVM);
    }

    public PictureFrameMenuVM getPictureFrameMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureFrameMenuVM);
    }

    public PictureTextMenuVM getPictureTextMenuVM() {
        return getChildBaseVM(CHILD_VM_mPictureTextMenuVM);
    }

}
