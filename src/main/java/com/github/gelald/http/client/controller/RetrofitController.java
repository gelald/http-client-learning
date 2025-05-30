package com.github.gelald.http.client.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.gelald.http.client.api.RetrofitClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@Tag(name = "Retrofit", description = "Retrofit component demo", extensions = {
        @Extension(properties = {@ExtensionProperty(name = "x-order", value = "800", parseValue = true)})
})
@RequestMapping("/retrofit")
public class RetrofitController {

    @Operation(summary = "Get", description = "Get demo")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject get(@RequestParam("param") String param) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                // connection timeout config
                .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
                // response timeout config
                .readTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://httpbin.org")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient).build();

        RetrofitClient retrofitClient = retrofit.create(RetrofitClient.class);
        // request param
        Call<JSONObject> call = retrofitClient.get(param);
        try {
            Response<JSONObject> execute = call.execute();
            if (execute.isSuccessful()) {
                JSONObject result = execute.body();
                log.info("result: {}", result);
                return result;
            } else {
                JSONObject obj = JSONUtil.createObj();
                obj.set("message", "error");
                return obj;
            }
        } catch (IOException exception) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            log.error("unexpected exception occurred", exception);
            return obj;
        }
    }

    @Operation(summary = "Post", description = "Post demo")
    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject post(@RequestBody JSONObject param) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                // connection timeout config
                .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
                // response timeout config
                .readTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://httpbin.org")
                .addConverterFactory(JacksonConverterFactory.create())
                .client(okHttpClient).build();

        RetrofitClient retrofitClient = retrofit.create(RetrofitClient.class);
        // request param
        Call<JSONObject> call = retrofitClient.post(param);
        try {
            Response<JSONObject> execute = call.execute();
            if (execute.isSuccessful()) {
                JSONObject result = execute.body();
                log.info("result: {}", result);
                return result;
            } else {
                JSONObject obj = JSONUtil.createObj();
                obj.set("message", "error");
                return obj;
            }
        } catch (IOException exception) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            log.error("unexpected exception occurred", exception);
            return obj;
        }
    }
}
