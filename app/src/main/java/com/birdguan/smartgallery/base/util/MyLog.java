package com.birdguan.smartgallery.base.util;

import android.util.Log;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 14:47
 */
public class MyLog {
    private static final String TAG = "SmartGallery/MyLog";

    public static void d(String tag, String method, String message, Object... objects) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(method).append(": ");

        if (message.contains(":") && objects != null && objects.length != 0) {
            String[] strings = message.split(":");
            for (int i = 0; i < Math.min(strings.length, objects.length); i++) {
                String name = strings[i];
                String dis = (objects[i] == null ? "null" : objects[i].toString());
                stringBuilder.append(name).append(":").append(dis).append(", ");
            }
        } else {
            stringBuilder.append(message);
        }
        Log.d(tag, stringBuilder.toString());
    }

    public static void d(String tag, String method, String message) {
        d(tag, method, message, (Object)null);
    }
}
