package com.github.gelald.http.client.controller;

import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@Tag(name = "Spring RestClient", description = "Spring RestClient component demo", extensions = {
        @Extension(properties = {@ExtensionProperty(name = "x-order", value = "600", parseValue = true)})
})
@RequestMapping("/rest-client")
public class RestClientController {

    @Operation(summary = "Get", description = "Get demo")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject get(@RequestParam("param") String param) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // connection timeout config
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.of(15, ChronoUnit.SECONDS));
        // response timeout config
        simpleClientHttpRequestFactory.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));
        RestClient restClient = RestClient.builder().requestFactory(simpleClientHttpRequestFactory).build();

        // request param
        JSONObject result = restClient.get()
                .uri(String.format("http://httpbin.org/get?param=%s", param))
                // request header
                .header(Header.ACCEPT.getValue(), "application/json")
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .exchange((request, response) -> {
                    if (response.getStatusCode().equals(HttpStatus.OK)) {
                        String body = response.bodyTo(String.class);
                        JSONObject okResult = JSONUtil.parseObj(body);
                        return okResult;
                    } else {
                        JSONObject errorResult = JSONUtil.createObj();
                        errorResult.set("message", "error");
                        return errorResult;
                    }
                });
        log.info("result: {}", result);
        return result;
    }

    @Operation(summary = "Post", description = "Post demo")
    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject post(@RequestBody JSONObject param) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // connection timeout config
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.of(15, ChronoUnit.SECONDS));
        // response timeout config
        simpleClientHttpRequestFactory.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));
        RestClient restClient = RestClient.builder().requestFactory(simpleClientHttpRequestFactory).build();

        JSONObject result = restClient.post()
                .uri("http://httpbin.org/post")
                // request header
                .header(Header.ACCEPT.getValue(), "application/json")
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .header(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8")
                .body(param)
                .exchange((request, response) -> {
                    if (response.getStatusCode().equals(HttpStatus.OK)) {
                        String body = response.bodyTo(String.class);
                        JSONObject okResult = JSONUtil.parseObj(body);
                        return okResult;
                    } else {
                        JSONObject errorResult = JSONUtil.createObj();
                        errorResult.set("message", "error");
                        return errorResult;
                    }
                });
        log.info("result: {}", result);
        return result;
    }
}
