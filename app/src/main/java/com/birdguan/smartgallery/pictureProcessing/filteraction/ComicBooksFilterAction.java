package com.birdguan.smartgallery.pictureProcessing.filteraction;

import org.opencv.core.Mat;

/**
 * @Author: birdguan
 * @Date: 2020/6/9 9:46
 */
public class ComicBooksFilterAction implements FilterAction {
    private static ComicBooksFilterAction INSTANCE = new ComicBooksFilterAction();
    private static final String NAME = "连环画";

    private ComicBooksFilterAction() {}

    public static ComicBooksFilterAction getInstance() {
        return INSTANCE;
    }

    private native void filterComicBooks(long in, long out);

    @Override
    public void filter(Mat oldMat, Mat newMat) {
        filterComicBooks(oldMat.getNativeObjAddr(), newMat.getNativeObjAddr());
    }

    @Override
    public String getFilterName() {
        return NAME;
    }
}
