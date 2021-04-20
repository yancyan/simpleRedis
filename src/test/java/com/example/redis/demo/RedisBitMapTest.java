package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.template.DefaultRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

/**
 * @author ZhuYX
 * @date 2021/04/16
 */
@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisBitMapTest {
    public static final String key = "redis-string:bit-map";
    @Autowired
    private DefaultRedisTemplate defaultRedisTemplate;

    @Test
    public void test() {
        var opsForValue = defaultRedisTemplate.opsForValue();
        var aBoolean = opsForValue.setBit(key, Integer.MAX_VALUE, true);
        System.out.println(aBoolean);
        System.out.println();
    }

}
