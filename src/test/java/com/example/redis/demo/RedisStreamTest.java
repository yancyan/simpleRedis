package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.domain.Person;
import com.example.redis.template.DefaultRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.net.InetAddress;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisStreamTest {
    public static final String key = "redis-stream";
    public static final String groupOne = key + "groupOne";


    @Autowired
    private DefaultRedisTemplate defaultRedisTemplate;

    @Test
    void redis_consumer() {
        var opsForStream = defaultRedisTemplate.opsForStream();
        var infoConsumers = opsForStream.consumers(key, groupOne);
        System.out.println(infoConsumers);

    }

    @Test
    public void consumer() {
        var opsForStream = defaultRedisTemplate.opsForStream();

        var records = opsForStream.read(Consumer.from(groupOne, "printConsumer"),
                StreamReadOptions.empty().count(20),
                StreamOffset.create(key, ReadOffset.lastConsumed()));

        System.out.println("########## " + records);

        opsForStream.acknowledge(key, groupOne, records.stream().map(x -> x.getId().toString()).toArray(String[]::new));

    }

    @Test
    void redis_test_() {
        var opsForStream = defaultRedisTemplate.opsForStream();
        // opsForStream.createGroup(key, groupOne);

        for (int i = 0; i < 10; i++) {

            var ofMap =
                    StreamRecords.newRecord().in(key).ofMap(Map.of("customerId", i
                    , "customerName", "lihua " + i));
            opsForStream.add(ofMap);
        }


        var listenerContainer = StreamMessageListenerContainer
                .create(Objects.requireNonNull(defaultRedisTemplate.getConnectionFactory()), StreamMessageListenerContainer
                        .StreamMessageListenerContainerOptions
                        .builder()
                        .pollTimeout(Duration.ofSeconds(1))
                        .targetType(Map.class)
                        .build());
        var autoAck = listenerContainer.receiveAutoAck(
                Consumer.from(groupOne, key),
                StreamOffset.create(key, ReadOffset.lastConsumed()),
                (StreamListener<String, ObjectRecord<String, Map>>) listenerContainer);
        listenerContainer.start();

    }

}
