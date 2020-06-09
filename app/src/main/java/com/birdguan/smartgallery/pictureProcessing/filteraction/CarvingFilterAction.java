package com.birdguan.smartgallery.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/9 9:45
 */
public class CarvingFilterAction implements FilterAction {
    private static CarvingFilterAction INSTANCE = new CarvingFilterAction();
    private static final String NAME = "雕刻";

    private CarvingFilterAction() {
    }

    public static CarvingFilterAction getInstance() {
        return INSTANCE;
    }

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterCarving(oldMat.getNativeObjAddr() , newMat.getNativeObjAddr());
    }

    private native void filterCarving(long in, long out);

    @Override
    public String getFilterName() {
        return NAME;
    }
}
