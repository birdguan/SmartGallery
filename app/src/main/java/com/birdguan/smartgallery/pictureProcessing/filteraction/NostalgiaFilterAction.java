package com.birdguan.smartgallery.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/9 9:50
 */
public class NostalgiaFilterAction implements FilterAction {
    private static NostalgiaFilterAction INSTANCE = new NostalgiaFilterAction();
    private static final String NAME = "雕刻";

    private NostalgiaFilterAction() {}

    public static NostalgiaFilterAction getInstance() {
        return INSTANCE;
    }

    private native void filterNostalgia(long in, long out);

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterNostalgia(oldMat.getNativeObjAddr(), newMat.getNativeObjAddr());
    }

    @Override
    public String getFilterName() {
        return NAME;
    }
}
