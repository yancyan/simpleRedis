package com.example.redis.demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * @author ZhuYX
 * @date 2021/04/21
 */
// @SpringBootTest(classes = RedisDemoApplication.class)
public class RestTemplateTest {

    @Test
    public void test() {

        URI uri = UriComponentsBuilder
                .fromUriString("https://example.com/hotels/{hotel}")
                .queryParam("q", "{q}")
                .encode()
                .buildAndExpand("Westin", "123")
                .toUri();
        System.out.println(uri);

        URI uri_1 = UriComponentsBuilder
                .fromUriString("https://example.com/hotels/{hotel}")
                .queryParam("q", "{q}")
                .build("Westin", "123");
        System.out.println(uri_1);

        var uriBuilderFactory = new DefaultUriBuilderFactory();
        var build = uriBuilderFactory
                // .builder()
                .uriString("https://example.com/hotels/{hotel}")
                .queryParam("q", "{q}")
                .build("Westin", "123");
        System.out.println(build);

        var restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(uriBuilderFactory);

    }

    @Test
    public void test1() {

    }
}
