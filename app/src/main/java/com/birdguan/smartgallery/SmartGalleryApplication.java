package com.birdguan.smartgallery;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

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
    }
}
