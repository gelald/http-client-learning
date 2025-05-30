package com.github.gelald.http.client;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ngwingbun
 * date: 2025/5/28
 */
@EnableKnife4j
@SpringBootApplication
public class HttpClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(HttpClientApplication.class, args);
    }
}
