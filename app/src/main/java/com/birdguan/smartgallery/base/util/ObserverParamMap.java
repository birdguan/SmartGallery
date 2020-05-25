package com.birdguan.smartgallery.base.util;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import java.util.HashMap;
import java.util.Map;

import static com.birdguan.smartgallery.staticParam.ObserverMapKey.SHOW_TOAST_MESSAGE;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 16:30
 */
public class ObserverParamMap {
    public static final String TAG = "SmartGallery: ObserverParamMap";
    private Map<Object, Object> mObjectMap = new HashMap<>();

    public static ObserverParamMap setToastMessage(String message) {
        ObserverParamMap observerParamMap = new ObserverParamMap();
        observerParamMap.mObjectMap.put(SHOW_TOAST_MESSAGE, message);
        return observerParamMap;
    }

    public static String getToastMessage(Observable observable) {
        ObserverParamMap observerParamMap = ((ObservableField<ObserverParamMap>) observable).get();
        String message = (String) observerParamMap.mObjectMap.get(SHOW_TOAST_MESSAGE);
        return message;
    }

    public static ObserverParamMap staticSet(Object key, Object value) throws RuntimeException {
        if (key == null || value == null) {
            throw new RuntimeException("创建observerParamMap时,key和value不可为null");
        }
        ObserverParamMap observerParamMap = new ObserverParamMap();
        observerParamMap.mObjectMap.put(key, value);
        return observerParamMap;
    }

    public static <V> V staticGetValue(Observable observable, Object key) throws RuntimeException {
        if (observable == null || key == null) {
            throw new RuntimeException("获取observerParamMap时，observerParamMap和key不能为null");
        }

        ObserverParamMap observerParamMap;
        try {
            observerParamMap = ((ObservableField<ObserverParamMap>) observable).get();
        } catch (Exception e) {
            throw new RuntimeException("获取observer参数时，传入的参数必须为observerParamMap");
        }

        if (observerParamMap == null) {
            MyLog.d(TAG, "staticGetValue", "状态:",
                    "传入的observerParamMap为空");
            return null;
        }

        V v;
        try {
            v = (V) observerParamMap.mObjectMap.get(key);
        } catch (Exception e) {
            throw new RuntimeException("创建observerParamMap时，传入的V与目前所需类型不符");
        }
        if (v == null) {
            MyLog.d(TAG, "staticGetValue", "状态:", "传入的参数为空");
            return null;
        }
        return v;
    }

    public ObserverParamMap() {}

    public ObserverParamMap set(Object key, Object value) throws RuntimeException {
        if (key == null || value == null) {
            throw new RuntimeException("创建observerParamMap的时候，key和value不可为null");
        }
        mObjectMap.put(key, value);
        return this;
    }

    public <V> V getValue(Object key) throws RuntimeException {
        if (key == null) {
            throw new RuntimeException("获取observerParamMap时，key不能为空");
        }

        V v;
        try {
            v = (V) mObjectMap.get(key);
        } catch (Exception e) {
            throw new RuntimeException("创建observerParamMap时，传入的V类型与所需类型不符");
        }
        if (v == null) {
            throw new RuntimeException("创建observerParamMap时，传入的V不能为空");
        }
        return v;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ObserverParamMap{");
        stringBuilder.append("mObjectMap=[");
        for (Object obj : mObjectMap.keySet()) {
            stringBuilder.append("key: ");
            stringBuilder.append(obj.toString());
            stringBuilder.append(",");
            stringBuilder.append("value:");
            stringBuilder.append(mObjectMap.get(obj).toString());
            stringBuilder.append(" ");
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
