package com.github.gelald.http.client.controller;

import cn.hutool.http.Header;
import cn.hutool.http.Method;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Slf4j
@RestController
@RequestMapping("/url-connection")
public class URLConnectionController {
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object get(@RequestParam("param") String param) throws IOException {
        // request param
        URL url = new URL(String.format("http://httpbin.org/get?param=%s", param));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // connection timeout config
        conn.setConnectTimeout(15 * 1000);
        // response timeout config
        conn.setReadTimeout(30 * 1000);

        conn.setRequestMethod(Method.GET.name());
        // request header
        conn.setRequestProperty(Header.ACCEPT.getValue(), "application/json");
        conn.setRequestProperty(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            return obj;
        }

        try (InputStream is = conn.getInputStream();
             Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        } finally {
            conn.disconnect();
        }
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object post(@RequestBody JSONObject jsonObject) throws IOException {
        URL url = new URL("http://httpbin.org/post");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // connection timeout config
        conn.setConnectTimeout(15 * 1000);
        // response timeout config
        conn.setReadTimeout(30 * 1000);

        conn.setRequestMethod(Method.POST.name());
        // request header
        conn.setRequestProperty(Header.ACCEPT.getValue(), "application/json");
        conn.setRequestProperty(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        conn.setRequestProperty(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8");
        // request body
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.info("can not write data into request", e);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            return obj;
        }

        try (InputStream is = conn.getInputStream();
             Scanner scanner = new Scanner(is, StandardCharsets.UTF_8)) {
            return scanner.useDelimiter("\\A").next();
        } finally {
            conn.disconnect();
        }
    }
}
