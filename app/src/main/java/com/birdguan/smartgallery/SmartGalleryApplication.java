package com.birdguan.smartgallery;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.birdguan.smartgallery.base.ITypefaceFetch;
import com.birdguan.smartgallery.base.util.MyUtil;
import com.birdguan.smartgallery.pictureProcessing.filteraction.FilterAction;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.birdguan.smartgallery.base.ITypefaceFetch.init;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.FEATHERS_IMAGE;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.FEATHERS_LOW_ASSET_IMAGE_NAME;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.SCREAM_IMAGE;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.SCREAM_LOW_ASSET_IMAGE_NAME;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.SKETCH_IMAGE;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.SKETCH_LOW_ASSET_IMAGE_NAME;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.STARRY_IMAGE;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.STARRY_LOW_ASSET_IMAGE_NAME;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.WAVE_IMAGE;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.WAVE_LOW_ASSET_IMAGE_NAME;
import static com.birdguan.smartgallery.staticParam.StaticParam.LOADING_ASSET_GIF_NAME;
import static com.birdguan.smartgallery.staticParam.StaticParam.LOADING_GIF;
import static com.birdguan.smartgallery.staticParam.StaticParam.MY_PHOTO_SHOP_DIRECTORY;
import static com.birdguan.smartgallery.staticParam.StaticParam.MY_SHARE_DIRECTORY;
import static com.birdguan.smartgallery.staticParam.StaticParam.PICTURE_FILTER_SAMPLE_ASSET_IMAGE_NAME;
import static com.birdguan.smartgallery.staticParam.StaticParam.PICTURE_FILTER_SAMPLE_IMAGE;
import static com.birdguan.smartgallery.staticParam.StaticParam.PICTURE_FRAME_ADD_ASSET_IMAGE_NAME;
import static com.birdguan.smartgallery.staticParam.StaticParam.PICTURE_FRAME_ADD_IMAGE;

public class SmartGalleryApplication extends Application {
    private static final String TAG = "SmartGallery/SmartGalleryApplication";

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        ImagePipelineConfig config =ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

        ITypefaceFetch.init();
        FilterAction.init();
        Log.e("INIT", "FilterAction init completed");
        init();
        Log.e("INIT", "init completed");



        File file = new File(MY_SHARE_DIRECTORY);
        if (!file.exists()) {
            file.mkdir();
        }

        File file1 = new File(MY_PHOTO_SHOP_DIRECTORY);
        if (!file1.exists()) {
            file1.mkdir();
        }
    }

    public static Context getAppContext() {
        return appContext;
    }

    // 将资源变成文件形式
    public static Map<String , String> NeedToFileImageNameMap = new HashMap<>();

    void init() {
        NeedToFileImageNameMap.put(PICTURE_FILTER_SAMPLE_IMAGE , PICTURE_FILTER_SAMPLE_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(PICTURE_FRAME_ADD_IMAGE, PICTURE_FRAME_ADD_ASSET_IMAGE_NAME);

        NeedToFileImageNameMap.put(STARRY_IMAGE , STARRY_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(FEATHERS_IMAGE , FEATHERS_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(WAVE_IMAGE , WAVE_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(SCREAM_IMAGE , SCREAM_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(SKETCH_IMAGE , SKETCH_LOW_ASSET_IMAGE_NAME);
        NeedToFileImageNameMap.put(LOADING_GIF, LOADING_ASSET_GIF_NAME);

        for (String filePath : NeedToFileImageNameMap.keySet()) {
            File pictureFilterSampleImage = new File(filePath);
            if (!pictureFilterSampleImage.exists()) {
                MyUtil.assetToFile(NeedToFileImageNameMap.get(filePath) ,filePath);
            }
        }
    }
}
