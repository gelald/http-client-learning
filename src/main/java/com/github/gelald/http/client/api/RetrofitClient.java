package com.github.gelald.http.client.api;

import cn.hutool.json.JSONObject;
import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitClient {
    @GET("/get")
    @Headers({
            "Accept: application/json",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36"
    })
    Call<JSONObject> get(@Query("param") String param);

    @POST("/post")
    @Headers({
            "Accept: application/json",
            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36",
            "Content-Type: application/json; charset=UTF-8"
    })
    Call<JSONObject> post(@Body JSONObject param);
}
