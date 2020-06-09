package com.birdguan.smartgallery.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/9 9:54
 */
public class ReliefFilterAction implements FilterAction {
    private static ReliefFilterAction INSTANCE = new ReliefFilterAction();
    private static final String NAME = "浮雕";

    private ReliefFilterAction() {}

    public static ReliefFilterAction getInstance() {
        return INSTANCE;
    }

    private native void filterRelief(long in, long out);

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterRelief(oldMat.getNativeObjAddr(), newMat.getNativeObjAddr());
    }

    @Override
    public String getFilterName() {
        return NAME;
    }
}
