package com.birdguan.smartgallery;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import static com.birdguan.smartgallery.base.ITypefaceFetch.init;

public class SmartGalleryApplication extends Application {
    private static final String TAG = "SmartGallery: SmartGalleryApplication"；

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        ImagePipelineConfig config =ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, config);

        ItypefaceFetch.init();
        FilterAction.init();
        init();

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
}
