package com.github.gelald.http.client.controller;

import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/web-client")
public class WebClientController {
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<JSONObject> get(@RequestParam("param") String param) {
        HttpClient httpClient = HttpClient.create()
                // connection timeout config
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15 * 1000)
                .doOnConnected(connection -> connection
                        // response timeout config
                        .addHandlerFirst(new ReadTimeoutHandler(30, TimeUnit.SECONDS)));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        Mono<JSONObject> result = client
                .get()
                // request param
                .uri(String.format("http://httpbin.org/get?param=%s", param))
                // request header
                .header(Header.ACCEPT.getValue(), "application/json")
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        Mono<JSONObject> okResult = clientResponse.bodyToMono(JSONObject.class);
                        return okResult;
                    } else {
                        return clientResponse.createException().flatMap(Mono::error);
                    }
                });

        log.info("result: {}", result.block());
        return result;
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<JSONObject> post(@RequestBody JSONObject param) {
        HttpClient httpClient = HttpClient.create()
                // connection timeout config
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 15 * 1000)
                .doOnConnected(connection -> connection
                        // response timeout config
                        .addHandlerFirst(new ReadTimeoutHandler(30, TimeUnit.SECONDS)));
        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        Mono<JSONObject> result = client
                .post()
                .uri("http://httpbin.org/post")
                // request header
                .accept(MediaType.APPLICATION_JSON)
                .header(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .header(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8")
                // request body
                .body(BodyInserters.fromValue(param))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        Mono<JSONObject> okResult = clientResponse.bodyToMono(JSONObject.class);
                        return okResult;
                    } else {
                        return clientResponse.createException().flatMap(Mono::error);
                    }
                });

        log.info("result: {}", result.block());
        return result;
    }
}
