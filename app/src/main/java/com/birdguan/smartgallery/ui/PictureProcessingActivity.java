package com.birdguan.smartgallery.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.databinding.DataBindingUtil;

import com.birdguan.smartgallery.R;
import com.birdguan.smartgallery.base.BaseActivity;
import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.util.MyUtil;
import com.birdguan.smartgallery.base.util.ObserverParamMap;
import com.birdguan.smartgallery.pictureProcessing.StringConsumerChain;
import com.birdguan.smartgallery.viewModel.PictureProcessingActivityVM;
import com.birdguan.smartgallery.viewModel.includeLayoutVM.PictureTextParamDialogVM;

import static com.birdguan.smartgallery.base.BaseSeekBarRecycleViewVM.LEAVE_BSBRV_VM_LISTENER;
import static com.birdguan.smartgallery.base.viewmodel.ItemManagerBaseVM.CLICK_ITEM;
import static com.birdguan.smartgallery.staticParam.ObserverMapKey.PictureProcessingActivityVM_intent;
import static com.birdguan.smartgallery.staticParam.ObserverMapKey.PictureTextItemVM_mPictureTextParamDialogVM;
import static com.birdguan.smartgallery.staticParam.StaticParam.FONT_EDIT_VIEW_IMAGE;
import static com.birdguan.smartgallery.viewModel.PictureProcessingActivityVM.CLICK_BACK;
import static com.birdguan.smartgallery.viewModel.PictureProcessingActivityVM.CLICK_SAVE;
import static com.birdguan.smartgallery.viewModel.PictureProcessingActivityVM.CLICK_SHARE;

public class PictureProcessingActivity extends BaseActivity {
    private static final String TAG = "SmartGallery/PictureProcessingActivity";

    private com.birdguan.smartgallery.ui.PictureProcessingActivityBinding mPictureProcessingActivityBinding;
    private PictureProcessingActivityVM mPictureProcessingActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPictureProcessingActivityBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_picture_processing);
        mPictureProcessingActivityVM = new PictureProcessingActivityVM(getIntent().getStringExtra("imageUri"));
        mPictureProcessingActivityBinding.setViewModel(mPictureProcessingActivityVM);

        registeredViewModelFiledsObserver();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void registeredViewModelFiledsObserver() {
        // 监听各种需要显示toast的ViewModel
        showToast(mPictureProcessingActivityVM);
        showToast(mPictureProcessingActivityVM.getPictureParamMenuVM());
        showToast(mPictureProcessingActivityVM.getPictureTransformMenuVM());
        showToast(mPictureProcessingActivityVM.getPictureTextMenuVM());

        // 监听 CLICK_BACK 的点击，以退出当前activity
        initListener(mPictureProcessingActivityVM, (observable, i) -> {
            PictureProcessingActivity.this.finish();
            MyLog.d(TAG, "onPropertyChanged", "状态:", "在activity中监听 CLICK_BACK 的点击");
        } , CLICK_BACK);

        // 监听 CLICK_SHARE 的点击，分享图片
        initListener(mPictureProcessingActivityVM, (observable, i) -> {
            Intent intent = ObserverParamMap.staticGetValue(observable , PictureProcessingActivityVM_intent);
            startActivity(intent);
            MyLog.d(TAG, "onPropertyChanged", "状态:intent:", "监听 CLICK_SHARE 的点击，分享图片" , intent);
        } , CLICK_SHARE);

        // 监听 CLICK_SAVE 的点击
        initListener(mPictureProcessingActivityVM, (observable, i) -> {
            savedConsumerPosition = StringConsumerChain.getInstance().getConsumerPoint();
            MyLog.d(TAG, "onPropertyChanged", "状态:savedConsumerPosition:", "监听 CLICK_SAVE 的点击" , savedConsumerPosition);
        } , CLICK_SAVE);

        // 监听 PictureText中点击字体列表，以显示dialog
        initListener(mPictureProcessingActivityVM.getPictureTextMenuVM(), (observable, i) -> {
            PictureTextParamDialogVM pictureTextParamDialogVM = ObserverParamMap.staticGetValue(observable , PictureTextItemVM_mPictureTextParamDialogVM);
            PictureTextParamSelectDialog pictureTextParamSelectDialog = new PictureTextParamSelectDialog();
            pictureTextParamSelectDialog.setPictureTextParamDialogVM(pictureTextParamDialogVM);
            pictureTextParamSelectDialog.show(getFragmentManager());
            MyLog.d(TAG, "registeredViewModelFiledsObserver", "状态:pictureTextParamDialogVM:", "监听 PictureText中点击字体列表，以显示dialog" , pictureTextParamDialogVM);
        } , CLICK_ITEM);

        // 监听离开PictureText，以获取当前fontView的图像
        initListener(mPictureProcessingActivityVM.getPictureTextMenuVM(), (observable, i) -> {
            mPictureProcessingActivityBinding.pictureTextFontEditView.setFocusable(false);
            Bitmap bitmap = mPictureProcessingActivityBinding.pictureTextFontEditView.getCacheBitmapFromView();
            mPictureProcessingActivityBinding.pictureTextFontEditView.setFocusable(true);
            mPictureProcessingActivityBinding.pictureTextFontEditView.setFocusableInTouchMode(true);;
            mPictureProcessingActivityBinding.pictureTextFontEditView.requestFocus();
            mPictureProcessingActivityBinding.pictureTextFontEditView.findFocus();
            String path = FONT_EDIT_VIEW_IMAGE;
            MyUtil.saveBitmap(bitmap , path);
            MyLog.d(TAG, "registeredViewModelFiledsObserver", "状态:path:", "监听离开PictureText，以获取当前fontView的图像" , path);
        } , LEAVE_BSBRV_VM_LISTENER);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    int savedConsumerPosition = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (savedConsumerPosition  != StringConsumerChain.getInstance().getConsumerPoint()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提示")
                        .setMessage("该图片还未保存，是否决定退出？")
                        .setNegativeButton("取消", (dialog, which) -> {
                        })
                        .setPositiveButton("确定", (dialog, which) -> PictureProcessingActivity.this.finish());
                builder.create().show();
                return true;
            } else {
                PictureProcessingActivity.this.finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        StringConsumerChain.getInstance().destroy();
        mPictureProcessingActivityVM.onCleared();
    }
}
