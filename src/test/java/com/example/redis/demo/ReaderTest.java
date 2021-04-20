package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.cache.config.SystemConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@SpringBootTest(classes = RedisDemoApplication.class)
public class ReaderTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test() throws IOException {
        var classPathResource = new ClassPathResource("cache/SystemConfig.json");
        var systemConfig = objectMapper.readValue(classPathResource.getInputStream(), SystemConfig.class);
        System.out.println(systemConfig);
    }




}
