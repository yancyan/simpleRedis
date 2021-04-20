package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.domain.Person;
import com.example.redis.domain.PersonWithIndexed;
import com.example.redis.repository.PersonMultiIdRepository;
import com.example.redis.repository.PersonRepository;
import com.example.redis.repository.PersonWithIndexedRepository;
import com.example.redis.template.DefaultRedisTemplate;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisStringTest {
    public static final String key = "redis-string";


    @Autowired
    private DefaultRedisTemplate defaultRedisTemplate;

    @Test
    void redis_test_() {
        var valueOps = defaultRedisTemplate.boundValueOps("redis-string");

        // valueOps.set("valueOps.setIfAbsent()");
        var p = Person.builder().id(15L).msg("test_key_1").build();
        valueOps.set(p);

        var person = valueOps.get();
        System.out.println(person);
    }

    @Test
    void redis_test_append() {
        var valueOps = defaultRedisTemplate.boundValueOps("redis-string");
        valueOps.append("ccc");
    }

    @Test
    void redis_test_increment() {
        var valueOps = defaultRedisTemplate.boundValueOps("redis-string");
        valueOps.set(0);
        System.out.println(valueOps.get());
        valueOps.increment(12);
        System.out.println(valueOps.get());
    }

    @Test
    void redis_test_opsValue() {
        var bitKey = key + ":bit";
        var valueOps = defaultRedisTemplate.opsForValue();
        valueOps.setBit(bitKey, 5, true);

        System.out.println(valueOps.getBit(bitKey, 5));
    }
}
