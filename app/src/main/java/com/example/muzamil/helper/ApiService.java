package com.example.muzamil.helper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("user_depan.php") // Sesuaikan endpoint dengan benar
    Call<ResponseBody> getUserData(@Field("nis") String nis); // Menggunakan ResponseBody untuk respons
}
