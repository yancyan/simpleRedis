package com.example.redis.demo;

import com.example.redis.RedisDemoApplication;
import com.example.redis.message.StreamMessage;
import com.example.redis.stream.Person;
import com.example.redis.stream.TestStreamListener;
import com.example.redis.template.DefaultRedisTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.hash.Jackson2HashMapper;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * @author ZhuYX
 * @date 2021/04/20
 */
@SuppressWarnings("unchecked")
@SpringBootTest(classes = RedisDemoApplication.class)
public class RedisStringStreamTest {
    // public static final String key = "redis-stream-1";
    public static final String key = RedisDemoApplication.key;

    public static final String group_prefix = key + "-group-";

    // public static final String defaultGroup = group_prefix + "default";
    public static final String defaultGroup = RedisDemoApplication.keyGroup;

    @Resource
    private DefaultRedisTemplate defaultRedisTemplate;


    @Test
    public void test_add() {

        var opsForStream = defaultRedisTemplate.opsForStream(new Jackson2HashMapper(false));
        if (opsForStream.groups(key).stream().noneMatch(dg -> defaultGroup.equals(dg.groupName()))) {
            opsForStream.createGroup(key, defaultGroup);
        }
        for (int i = 0; i < 100; i++) {
            opsForStream.add(StreamRecords.objectBacked(StreamMessage.builder()
                    .id(12L + i)
                    .name("name" + i)
                    .build()).withStreamKey(key));

        }

    }

    @Test
    public void test_receive() {

        var opsForStream = defaultRedisTemplate.opsForStream(new Jackson2HashMapper(false));

        if (opsForStream.groups(key).stream().noneMatch(dg -> defaultGroup.equals(dg.groupName()))) {
            opsForStream.createGroup(key, defaultGroup);
        }
        var pending = opsForStream.pending(key, defaultGroup);
        if (pending != null) {
            System.out.println("############### Pending Info: " + pending.getPendingMessagesPerConsumer());
        }

        var objectRecords = opsForStream.read(StreamMessage.class,
                Consumer.from(defaultGroup, key),
                StreamReadOptions.empty()
                        .autoAcknowledge().count(1)
                        // .block(Duration.ofSeconds(60))
                , StreamOffset.create(key, ReadOffset.lastConsumed()));

        System.out.println("############ message info: " + objectRecords);
    }

    @Resource
    private TestStreamListener listener;

    @Test
    public void test_receive_ () {
        for (int i = 100; i < 110; i++) {
            listener.sendMessage(Person.builder()
                    .id(Long.parseLong(i + ""))
                    .name("name_" + i)
                    .birthday(LocalDate.now())
                    .build());
        }
    }

    @Test
    public void test_receive_async_StreamReceiver () {

    }
}
