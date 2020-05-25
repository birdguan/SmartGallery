package com.birdguan.smartgallery.base;

import java.util.List;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 21:28
 */
public interface IImageUriFetch {
    List<String> getAllImageUriList();
    List<String> getRangeImageUriList(int start, int end);
    List<String> getAllImageUriListFromTag(Object tag);
    List<Object> getAllTag();
    void freshImageInfo();
}
