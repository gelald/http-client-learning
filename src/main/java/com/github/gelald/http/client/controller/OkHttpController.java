package com.github.gelald.http.client.controller;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@RequestMapping("/ok-http")
public class OkHttpController {
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject get(@RequestParam("param") String param) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                // connection timeout config
                .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
                // response timeout config
                .readTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .build();

        Request request = new Request.Builder()
                // request param
                .url(String.format("http://httpbin.org/get?param=%s", param))
                // request header
                .header(Header.ACCEPT.getValue(), "application/json")
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String string = new String(response.body().bytes());
                JSONObject result = JSONUtil.parseObj(string);
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

    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject post(@RequestBody JSONObject param) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                // connection timeout config
                .connectTimeout(Duration.of(15, ChronoUnit.SECONDS))
                // response timeout config
                .readTimeout(Duration.of(30, ChronoUnit.SECONDS))
                .build();

        // request body
        okhttp3.RequestBody requestBody = okhttp3.RequestBody.create(param.toString(), okhttp3.MediaType.parse(ContentType.JSON.getValue()));

        Request request = new Request.Builder()
                .url("http://httpbin.org/post")
                // request header
                .header(Header.ACCEPT.getValue(), "application/json")
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .header(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String string = new String(response.body().bytes());
                JSONObject result = JSONUtil.parseObj(string);
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
