package com.birdguan.smartgallery.pictureProcessing.filteraction;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.FEATHERS;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.SKETCH;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.STARRY;
import static com.birdguan.smartgallery.pictureProcessing.filteraction.AIFilterAction.WAVE;

/**
 * @Author: birdguan
 * @Date: 2020/6/8 21:58
 */
public interface FilterAction {

    List<FilterAction> FILTER_ACTION_LIST = new ArrayList<>();

    static void init() {

        FilterAction.addFilterAction(CarvingFilterAction.getInstance());
        FilterAction.addFilterAction(ReliefFilterAction.getInstance());
        FilterAction.addFilterAction(NostalgiaFilterAction.getInstance());
        FilterAction.addFilterAction(ComicBooksFilterAction.getInstance());
//        FilterAction.addFilterAction(new AIFilterAction(SCREAM));
        FilterAction.addFilterAction(new AIFilterAction(FEATHERS));
        FilterAction.addFilterAction(new AIFilterAction(STARRY));
        FilterAction.addFilterAction(new AIFilterAction(WAVE));
        FilterAction.addFilterAction(new AIFilterAction(SKETCH));
    }

    static void addFilterAction(FilterAction filterAction) {
        FILTER_ACTION_LIST.add(filterAction);
    }

    static List<FilterAction> getAllFilterAction() {
        return FILTER_ACTION_LIST;
    }

    void filter(Mat oldMat , Mat newMat);
    String getFilterName();
}
