package com.birdguan.smartgallery.base.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.birdguan.smartgallery.SmartGalleryApplication;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

import static com.birdguan.smartgallery.staticParam.StaticParam.DOWNLOAD_IMAGE;
import static org.opencv.imgproc.Imgproc.COLOR_BGR2RGBA;

/**
 * @Author: birdguan
 * @Date: 2020/5/25 18:03
 */
public class MyUtil {

    public static int getDisplayWidth() {
        Resources resources = SmartGalleryApplication.getAppContext().getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    public static int getDisplayWidthDp() {
        return MyUtil.px2dip(MyUtil.getDisplayWidth());
    }

    /**
     * 根据分辨率从dp转为px
     * @param dpValue
     * @return
     */
    public static int dip2px(float dpValue) {
        float scale = SmartGalleryApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * @param spValue
     * @return
     */
    public static int sp2px(float spValue) {
        final float fontScale = SmartGalleryApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率px转成dp
     * @param pxValue
     * @return
     */
    public static int px2dip(float pxValue) {
        float scale = SmartGalleryApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     * @param pxValue
     * @return
     */
    public static int px2sp(float pxValue) {
        final float fontScale = SmartGalleryApplication.getAppContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new int[]{options.outWidth, options.outHeight};
    }

    public static File getCacheDirectory(Context context, String type) {
        File appCacheDir = getExternalCacheDirectory(context, type);
        if (appCacheDir == null) {
            appCacheDir = getInternalCacheDirectory(context, type);
        }

        if (appCacheDir == null) {
            Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!appCacheDir.exists() && ! appCacheDir.mkdir()) {
                Log.e("getCacheDirectory","getCacheDirectory fail ,the reason is make directory fail !");
            }
        }
        return appCacheDir;
    }

    private static File getExternalCacheDirectory(Context context,String type) {
        File appCacheDir = null;
        if( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)){
                appCacheDir = context.getExternalCacheDir();
            }else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null){// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(),"Android/data/"+context.getPackageName()+"/cache/"+type);
            }

            if (appCacheDir == null){
                Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard unknown exception !");
            }else {
                if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
                    Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        }else {
            Log.e("getExternalDirectory","getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }

    private static File getInternalCacheDirectory(Context context,String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)){
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        }else {
            appCacheDir = new File(context.getFilesDir(),type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists()&&!appCacheDir.mkdirs()){
            Log.e("getInternalDirectory","getInternalDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }

    public static void saveBitmap(Bitmap bitmap , String path) {
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.PNG , 90 , out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("保存图片失败");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("保存图片失败");
        }

//        try {
//            MediaStore.Images.Media.insertImage(PictureProcessingApplication.getAppContext().getContentResolver(),
//                    file.getAbsolutePath(), file.getName() , null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        // 最后通知图库更新
        SmartGalleryApplication.getAppContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
    }

    public static Mat matBgrToRgba(Mat src) {
        Mat matRgba = new Mat();
        if (src.channels() == 3 || src.channels() == 4) {
            Imgproc.cvtColor(src , matRgba , COLOR_BGR2RGBA);
            return matRgba;
        } else {
            return src;
        }
    }

    public static boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(DOWNLOAD_IMAGE);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static Bitmap getBitmapFromAsset(String imageName){
        Bitmap bitmap;
        InputStream ims = null;
        try {
            // get input stream
            ims = SmartGalleryApplication.getAppContext().getAssets().open(imageName);
            // load image as Drawable
            bitmap = BitmapFactory.decodeStream(ims);
            // set image to ImageView
        } catch(IOException ex) {
            throw new RuntimeException("从asset中读取图片失败");
        } finally {
            try {
                assert ims != null;
                ims.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static void assetToFile(String imageName, String filePath){
        InputStream ims = null;
        FileOutputStream fileOutputStream = null;
        try {
            // get input stream
            ims = SmartGalleryApplication.getAppContext().getAssets().open(imageName);
            File file = new File(filePath);
            if(file.exists()){
                file.delete();
            }
            fileOutputStream = new FileOutputStream(filePath);
            byte[] buffer = new byte[512];
            int count = 0;
            while((count = ims.read(buffer)) > 0){
                fileOutputStream.write(buffer, 0 ,count);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("将asset写入文件失败");
        } finally {
            try {
                if (fileOutputStream != null && ims != null) {
                    fileOutputStream.close();
                    ims.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
