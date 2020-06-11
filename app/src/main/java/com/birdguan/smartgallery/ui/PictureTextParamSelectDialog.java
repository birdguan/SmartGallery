package com.birdguan.smartgallery.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.birdguan.smartgallery.R;
import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.databinding.ActivityPictureProcessingPictureTextParamDialogBinding;
import com.birdguan.smartgallery.viewModel.includeLayoutVM.PictureTextParamDialogVM;

/**
 * @Author: birdguan
 * @Date: 2020/6/9 10:01
 */
public class PictureTextParamSelectDialog extends DialogFragment {
    private static final String TAG = "SmartGallery/PictureTextParamSelectDialog";

    private PictureTextParamDialogVM mPictureTextParamDialogVM;
    private ActivityPictureProcessingPictureTextParamDialogBinding mActivityPictureProcessingPictureTextParamDialogBinding;

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, "ViewDialogFragment");
    }

    public void setPictureTextParamDialogVM(PictureTextParamDialogVM pictureTextParamDialogVM) {
        mPictureTextParamDialogVM = pictureTextParamDialogVM;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mActivityPictureProcessingPictureTextParamDialogBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater() , R.layout.activity_picture_processing_picture_text_param_dialog , null , false);
        mActivityPictureProcessingPictureTextParamDialogBinding.setViewModel(mPictureTextParamDialogVM);
        mPictureTextParamDialogVM.resume();
        builder.setView(mActivityPictureProcessingPictureTextParamDialogBinding.getRoot())
                .setPositiveButton("确定", (dialog, which) -> {
                    mPictureTextParamDialogVM.stop();
                    MyLog.d(TAG, "onTextChanged", "状态:", "更改字体大小和颜色");
                }).setNegativeButton("取消", (dialog, which) -> {
            MyLog.d(TAG, "onTextChanged", "状态:", "取消更改字体大小和颜色");
        });
        return builder.create();
    }
}
