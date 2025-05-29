package com.github.gelald.http.client.controller;

import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@Tag(name = "Spring RestTemplate")
@RequestMapping("/rest-template")
public class RestTemplateController {
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject get(@RequestParam("param") String param) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // connection timeout config
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.of(15, ChronoUnit.SECONDS));
        // response timeout config
        simpleClientHttpRequestFactory.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        // request header
        header.add(Header.ACCEPT.getValue(), "application/json");
        header.add(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        HttpEntity<?> httpEntity = new HttpEntity<>(header);
        // request param
        ResponseEntity<String> responseEntity = restTemplate.exchange(String.format("http://httpbin.org/get?param=%s", param), HttpMethod.GET, httpEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String body = responseEntity.getBody();
            JSONObject result = JSONUtil.parseObj(body);
            log.info("result: {}", result);
            return result;
        } else {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            return obj;
        }
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject post(@RequestBody JSONObject param) {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        // connection timeout config
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.of(15, ChronoUnit.SECONDS));
        // response timeout config
        simpleClientHttpRequestFactory.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));
        RestTemplate restTemplate = new RestTemplate(simpleClientHttpRequestFactory);

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        // request header
        header.add(Header.ACCEPT.getValue(), "application/json");
        header.add(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36");
        header.add(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8");
        // request body
        HttpEntity<?> httpEntity = new HttpEntity<>(param, header);
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://httpbin.org/post", HttpMethod.POST, httpEntity, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            String body = responseEntity.getBody();
            JSONObject result = JSONUtil.parseObj(body);
            log.info("result: {}", result);
            return result;
        } else {
            JSONObject obj = JSONUtil.createObj();
            obj.set("message", "error");
            return obj;
        }
    }
}
