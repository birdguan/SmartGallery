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
    List<String> getRangeImageUriListFromTag(Object tag , int start , int end);
    List<Object> getAllTag();
    void freshImageInfo();
}
