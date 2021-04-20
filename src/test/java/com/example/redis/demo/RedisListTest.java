package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.domain.Person;
import com.example.redis.template.DefaultRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisListTest {
    public static final String key = "redis-list";


    @Autowired
    private DefaultRedisTemplate defaultRedisTemplate;

    @Test
    void redis_test_() {
        var opsForList = defaultRedisTemplate.opsForList();
        opsForList.rightPush(key, "rPush1");
        opsForList.rightPush(key, "rPush2");
        opsForList.rightPush(key, "rPush3");
        opsForList.rightPush(key, "rPush4");
        opsForList.rightPush(key, "rPush5");
        opsForList.trim(key, 0, 3);
    }

}
