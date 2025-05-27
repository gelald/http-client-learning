package com.github.gelald.http.client.controller;

import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/apache-http-client")
public class ApacheHttpClientController {
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject get(@RequestParam("param") String param) throws URISyntaxException {
        // request param
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("param", param));
        URI uri = new URIBuilder(new URI("http://httpbin.org/get"))
                .addParameters(parameters).build();
        HttpGet httpGet = new HttpGet(uri);
        // request header
        httpGet.addHeader(Header.ACCEPT.getValue(), "application/json");
        httpGet.addHeader(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");

        RequestConfig config = RequestConfig.custom()
                // connection timeout config
                .setConnectTimeout(Timeout.ofSeconds(15))
                // response timeout config
                .setResponseTimeout(Timeout.ofSeconds(30)).build();

        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            try (CloseableHttpResponse response = client.execute(httpGet)) {
                if (response.getCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    JSONObject result = JSONUtil.parseObj(EntityUtils.toString(entity));
                    log.info("result: {}", result);
                    return result;
                } else {
                    JSONObject obj = JSONUtil.createObj();
                    obj.set("message", "error");
                    return obj;
                }
            }
        } catch (IOException | ParseException exception) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            log.error("unexpected exception occurred", exception);
            return obj;
        }
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject post(@RequestBody JSONObject param) throws URISyntaxException {
        URI uri = new URIBuilder(new URI("http://httpbin.org/post")).build();
        HttpPost httpPost = new HttpPost(uri);
        // request body
        httpPost.setEntity(new StringEntity(param.toString(), ContentType.APPLICATION_JSON));
        // request header
        httpPost.addHeader(Header.ACCEPT.getValue(), "application/json");
        httpPost.addHeader(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        httpPost.addHeader(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8");

        RequestConfig config = RequestConfig.custom()
                // connection timeout config
                .setConnectTimeout(Timeout.ofSeconds(15))
                // response timeout config
                .setResponseTimeout(Timeout.ofSeconds(30)).build();

        try (CloseableHttpClient client = HttpClients.custom().setDefaultRequestConfig(config).build()) {
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                if (response.getCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    JSONObject result = JSONUtil.parseObj(EntityUtils.toString(entity));
                    log.info("result: {}", result);
                    return result;
                } else {
                    JSONObject obj = JSONUtil.createObj();
                    obj.set("message", "error");
                    return obj;
                }
            }
        } catch (IOException | ParseException exception) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            log.error("unexpected exception occurred", exception);
            return obj;
        }
    }
}
