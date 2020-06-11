package com.birdguan.smartgallery.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Spinner;

import com.birdguan.smartgallery.R;
import com.birdguan.smartgallery.base.BaseActivity;
import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.util.ObserverParamMap;
import com.birdguan.smartgallery.viewModel.MainActivityVM;
import com.birdguan.smartgallery.viewModel.itemManagerVM.DirectorySpinnerItemManagerVM;
import com.tbruyelle.rxpermissions2.RxPermissions;

import static com.birdguan.smartgallery.base.viewmodel.ItemManagerBaseVM.CLICK_ITEM;
import static com.birdguan.smartgallery.staticParam.ObserverMapKey.DirectorySpinnerItemManagerVM_directoryName;
import static com.birdguan.smartgallery.staticParam.ObserverMapKey.PictureItemManagerVM_mImageUri;

public class MainActivity extends BaseActivity {
    private static final String TAG = "SmartGallery/MainActivity";

    private com.birdguan.smartgallery.ui.MainActivityBinding mMainActivityBinding;
    private MainActivityVM mMainActivityVM;

    static  {
        System.loadLibrary("native-lib");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final boolean[] isAllowedPermission = {false};
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(
                Manifest.permission.READ_EXTERNAL_STORAGE ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .filter(permission -> permission.granted)
                .subscribe(permission -> {
                    if (!isAllowedPermission[0]) {
                        mMainActivityBinding = DataBindingUtil.setContentView(MainActivity.this , R.layout.activity_main);
                        mMainActivityVM = getViewModel(MainActivityVM.class);
                        mMainActivityBinding.setViewModel(mMainActivityVM);

                        registerViewModelFieldsObserver();
                    }
                    isAllowedPermission[0] = true;
                });
    }

    @Override
    protected void registerViewModelFieldsObserver() {
        // 监听bar上面目录切换时候的toast显示
        showToast(mMainActivityVM.getDirectorySpinnerItemManagerVM());

        // 监听列表中item的点击事件
        initListener(mMainActivityVM.getPictureItemManagerVM() , (observable, i) -> {
            String imageUri = ObserverParamMap.staticGetValue(observable , PictureItemManagerVM_mImageUri);
            Intent intent = new Intent(MainActivity.this , PictureProcessingActivity.class);
            intent.putExtra("imageUri" , imageUri);
            MainActivity.this.startActivity(intent);

            MyLog.d(TAG, "onPropertyChanged", "状态:imageUri:", "监听列表中item的点击事件" , imageUri);
        } , CLICK_ITEM);

        // 监听bar上面的目录切换事件
        initListener(mMainActivityVM.getDirectorySpinnerItemManagerVM() , (observable, i) -> {
            String directoryName = ObserverParamMap.staticGetValue(observable, DirectorySpinnerItemManagerVM_directoryName);
            mMainActivityVM.getPictureItemManagerVM().freshPictureList(directoryName);
            MyLog.d(TAG, "onPropertyChanged", "状态:directoryName:", "监听bar上面的目录切换事件" , directoryName);
        } , CLICK_ITEM);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainActivityVM.onCleared();
    }

}

