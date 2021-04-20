package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.template.DefaultRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

/**
 * @author ZhuYX
 * @date 2021/04/20
 */
@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisZSetTest {
    public static final String key = "redis-zset";

    @Autowired
    private DefaultRedisTemplate defaultRedisTemplate;

    @Test
    public void test() {
        var opsForZSet = defaultRedisTemplate.opsForZSet();
        // opsForZSet.add(key, "zSetValue12", 12);
        // opsForZSet.add(key, "zSetValue13", 13);
        // opsForZSet.add(key, "zSetValue14", 14);
        // opsForZSet.add(key, "zSetValue15", 15);
        // opsForZSet.add(key, "zSetValue16", 16);

        var count = opsForZSet.count(key, 12, 14);
        System.out.println(count);

        var range = opsForZSet.range(key, 1, 3);
        System.out.println(range);


    }
}
