package com.birdguan.smartgallery.viewModel;

import com.birdguan.smartgallery.base.viewmodel.ParentBaseVM;
import com.birdguan.smartgallery.viewModel.itemManagerVM.DirectorySpinnerItemManagerVM;
import com.birdguan.smartgallery.viewModel.itemManagerVM.PictureItemManagerVM;

public class MainActivityVM extends ParentBaseVM {
    private static final String TAG = "SmartGallery: MainActivityVM";

    public static final int CHILD_VM_PictureItemManagerVM = 0;
    public static final int CHILD_VM_DirectorySpinnerItemManagerVM = 1;

    public MainActivityVM() {
        initChildBaseVM(PictureItemManagerVM.class , CHILD_VM_PictureItemManagerVM);
        initChildBaseVM(DirectorySpinnerItemManagerVM.class , CHILD_VM_DirectorySpinnerItemManagerVM);
    }

    public PictureItemManagerVM getPictureItemManagerVM() {
        return getChildBaseVM(CHILD_VM_PictureItemManagerVM);
    }

    public DirectorySpinnerItemManagerVM getDirectorySpinnerItemManagerVM() {
        return getChildBaseVM(CHILD_VM_DirectorySpinnerItemManagerVM);
    }
}
