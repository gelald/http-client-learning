package com.github.gelald.http.client.controller;

import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@Tag(name = "JDK11 HttpClient", description = "JDK11 HttpClient component demo", extensions = {
        @Extension(properties = {@ExtensionProperty(name = "x-order", value = "200", parseValue = true)})
})
@RequestMapping("/http-client")
public class HttpClientController {

    @Operation(summary = "Get", description = "Get demo")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject get(@RequestParam("param") String param) throws URISyntaxException {
        HttpClient httpClient = HttpClient.newBuilder()
                // connection timeout config
                .connectTimeout(Duration.of(15, ChronoUnit.SECONDS)).build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                // request param
                .uri(new URI(String.format("http://httpbin.org/get?param=%s", param)))
                // request header
                .header(Header.ACCEPT.getValue(), "application/json")
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                // response timeout config
                .timeout(Duration.of(30, ChronoUnit.SECONDS))
                .GET().build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                JSONObject result = JSONUtil.parseObj(httpResponse.body());
                log.info("result: {}", result);
                return result;
            } else {
                JSONObject obj = JSONUtil.createObj();
                obj.set("message", "error");
                return obj;
            }
        } catch (IOException | InterruptedException exception) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            log.error("unexpected exception occurred", exception);
            return obj;
        }
    }

    @Operation(summary = "Post", description = "Post demo")
    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject post(@RequestBody JSONObject param) throws URISyntaxException {
        HttpClient httpClient = HttpClient.newBuilder()
                // connection timeout config
                .connectTimeout(Duration.of(15, ChronoUnit.SECONDS)).build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://httpbin.org/post"))
                // request header
                .header(Header.ACCEPT.getValue(), "application/json")
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .header(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8")
                // response timeout config
                .timeout(Duration.of(30, ChronoUnit.SECONDS))
                // request body
                .POST(HttpRequest.BodyPublishers.ofString(param.toString())).build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (httpResponse.statusCode() == 200) {
                JSONObject result = JSONUtil.parseObj(httpResponse.body());
                log.info("result: {}", result);
                return result;
            } else {
                JSONObject obj = JSONUtil.createObj();
                obj.set("message", "error");
                return obj;
            }
        } catch (IOException | InterruptedException exception) {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            log.error("unexpected exception occurred", exception);
            return obj;
        }
    }
}
