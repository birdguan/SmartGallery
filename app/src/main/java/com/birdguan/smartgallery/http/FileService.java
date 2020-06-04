package com.birdguan.smartgallery.http;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * @Author: birdguan
 * @Date: 2020/6/4 9:40
 */
public interface FileService {
    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);

    @GET
    Call<ResponseBody> download(@Url String fileUrl);
}
