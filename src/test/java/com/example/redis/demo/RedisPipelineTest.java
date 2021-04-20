package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.domain.Person;
import com.example.redis.template.DefaultRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.util.StopWatch;

import java.util.List;


@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisPipelineTest {
    public static final String key = "redis-string";


    @Autowired
    private DefaultRedisTemplate defaultRedisTemplate;


    @Test
    void redis_test_() {

        var watch = new StopWatch("watch");
        watch.start("pipeline");
        defaultRedisTemplate.executePipelined(new SessionCallback<String>() {
            @Override
            public String execute(RedisOperations operations) throws DataAccessException {

                var opsForHash = operations.opsForHash();
                for (int i = 0; i < 10000; i++) {
                    opsForHash.putIfAbsent( "test_batch_test1", "key_" + i, "value_ " + i);
                }
                return null;
            }
        });
        watch.stop();

        watch.start("opsForHash");
        for (int i = 0; i < 10000; i++) {
            // opsForHash.putIfAbsent( "test_", "key_" + i, "value_ " + i);
        defaultRedisTemplate.opsForHash().put("test_batch_test2", "key_" + i, "value_ " + i);
        }
        watch.stop();

        System.out.println(watch.prettyPrint());
    }

}
