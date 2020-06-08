package com.birdguan.smartgallery.pictureProcessing;

import android.text.TextUtils;

import com.birdguan.smartgallery.base.util.MyLog;
import com.birdguan.smartgallery.base.util.MyUtil;
import com.birdguan.smartgallery.impl.BaseMyConsumer;
import com.birdguan.smartgallery.impl.ConsumerChain;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;

import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:32
 */
public class StringConsumerChain extends ConsumerChain<String> {
    private static final String TAG = "SmartGalley: StringConsumerChain";

    private static StringConsumerChain INSTANCE;

    public static StringConsumerChain getInstance() {
        if (INSTANCE == null) {
            synchronized (StringConsumerChain.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StringConsumerChain();
                }
            }
        }
        return INSTANCE;
    }

    private StringConsumerChain() {
    }

    @Override
    protected Mat getStartResult(String path) {
        MyLog.d(TAG, "getStartResult", "状态:path:", "获取图片" , path);

        if (TextUtils.isEmpty(path)) {
            throw new RuntimeException("需要处理的图片路径为空");
        }

        File imageFile = new File(path);
        if (!imageFile.exists()) {
            throw new RuntimeException("需要处理的图片不存在");
        }

        int[] imageInfo = MyUtil.getImageWidthHeight(path);
        Mat firstMatBGR = Imgcodecs.imread(path , IMREAD_COLOR);

        Rect rect = new Rect(0 , 0 , imageInfo[0] , imageInfo[1]);
        BaseMyConsumer firstCutMyConsumer = new CutMyConsumer(rect) {
            @Override
            protected Mat onNewResultImpl(Mat oldResult) {
                return firstMatBGR;
            }
        };
        addConsumer(firstCutMyConsumer);

        MyLog.d(TAG, "getStartResult", "状态:firstMat:rect" , "获取图片结束" , firstMatBGR , rect);
        return firstMatBGR;
    }

}
