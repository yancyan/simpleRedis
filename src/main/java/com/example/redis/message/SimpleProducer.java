package com.example.redis.message;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisStreamCommands;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author ZhuYX
 * @date 2021/04/20
 */
public class SimpleProducer {


    private static final Logger LOGGER = LogManager.getLogger(SimpleProducer.class);

    public static void main(String[] args) throws Exception {

        RedisClient client = RedisClient.create(RedisURI.create("10.0.251.217", 6380));
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStreamCommands<String, String> commands = connection.sync();

        while (true) {

            Map<String, String> body = Collections.singletonMap("time", LocalDateTime.now().toString());
            LOGGER.info(String.format("Adding message with body: %s", body));

            commands.xadd("my_stream", body);

            Thread.sleep(1000);
        }
    }
}
