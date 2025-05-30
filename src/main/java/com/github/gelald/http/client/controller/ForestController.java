package com.github.gelald.http.client.controller;

import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@Tag(name = "Forest", description = "Forest component demo", extensions = {
        @Extension(properties = {@ExtensionProperty(name = "x-order", value = "900", parseValue = true)})
})
@RequestMapping("/forest")
public class ForestController {

    @Operation(summary = "Get", description = "Get demo")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject get(@RequestParam("param") String param) {
        ForestConfiguration config = Forest.config();
        // connection timeout config
        config.setConnectTimeout(Duration.of(15, ChronoUnit.SECONDS));
        // response timeout config
        config.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));

        JSONObject result = config
                .get("http://httpbin.org/get")
                // request param
                .addQuery("param", param)
                // request header
                .addHeader(Header.ACCEPT.getValue(), "application/json")
                .addHeader(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .execute(JSONObject.class);

        log.info("result: {}", result);
        return result;
    }

    @Operation(summary = "Post", description = "Post demo")
    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public JSONObject post(@RequestBody JSONObject param) {
        ForestConfiguration config = Forest.config();
        // connection timeout config
        config.setConnectTimeout(Duration.of(15, ChronoUnit.SECONDS));
        // response timeout config
        config.setReadTimeout(Duration.of(30, ChronoUnit.SECONDS));

        JSONObject result = config
                .post("http://httpbin.org/post")
                // request body
                .addBody(param)
                // request header
                .addHeader(Header.ACCEPT.getValue(), "application/json")
                .addHeader(Header.USER_AGENT.getValue(), "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .addHeader(Header.CONTENT_TYPE.getValue(), "application/json; charset=UTF-8")
                .execute(JSONObject.class);

        log.info("result: {}", result);
        return result;
    }
}
